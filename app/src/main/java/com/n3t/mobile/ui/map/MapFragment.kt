package com.n3t.mobile.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.android.gestures.StandardScaleGestureDetector
import com.mapbox.android.gestures.ShoveGestureDetector
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapView
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.RenderedQueryOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconRotationAlignment
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.OnScaleListener
import com.mapbox.maps.plugin.gestures.OnShoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.n3t.mobile.R
import com.n3t.mobile.data.model.common.MapStyleURL
import com.n3t.mobile.data.model.common.getStyleUrl
import com.n3t.mobile.data.model.place.StationModel
import com.n3t.mobile.data.model.place.StationProvider
import com.n3t.mobile.databinding.FragmentMapBinding
import com.n3t.mobile.ui.map.widget.ChooseMapTypeFragment
import com.n3t.mobile.ui.map.widget.StationInfoBottomSheet
import com.n3t.mobile.ui.search.SearchActivity
import com.n3t.mobile.utils.location.LocationPermissionHelper
import com.n3t.mobile.utils.location.MyLocationManager
import com.n3t.mobile.utils.Constants
import com.n3t.mobile.core.mapbox.navigation.NavigationLocationProvider
import com.n3t.mobile.ui.setting.SettingActivity
import com.n3t.mobile.view_model.map.MapViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * MapFragment — displays Mapbox map with location tracking, gestures, bottom sheet dashboard,
 * station display, search/routing, and map type switching.
 */
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by activityViewModel()

    // Map references
    private lateinit var mapView: MapView
    private var pointAnnotationManager: PointAnnotationManager? = null

    // Location
    private var locationManager: MyLocationManager? = null
    private var permissionHelper: LocationPermissionHelper? = null
    private val navigationLocationProvider = NavigationLocationProvider()

    // State
    private var isFollowCamera: Boolean = true
    private var isPitch: Boolean = false

    // Constants
    companion object {
        private const val FLY_DURATION = 800L

        // Station layer/source IDs
        private const val STATION_SOURCE_ID = "station_source"
        private const val STATION_LAYER_ID = "station_layer"
        private const val CHARGE_STATION_ICON = "charge_station_icon"
        private const val FUEL_STATION_ICON = "fuel_station_icon"
    }

    // region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        locationManager = MyLocationManager(requireContext())
        permissionHelper = LocationPermissionHelper(requireContext())

        setupMap()
        setupUI()
        setupBottomSheet()
        setupGestureListeners()
        listenAppEvents()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        locationManager?.stopLocationUpdates()
        pointAnnotationManager = null
        _binding = null
        super.onDestroyView()
    }

    // endregion

    // region Feature 1: Map Display

    private fun setupMap() {
        // Hide Mapbox UI chrome
        mapView.apply {
            logo.enabled = false
            attribution.enabled = false
            compass.enabled = false
            scalebar.enabled = false
        }

        mapView.setMaximumFps(30)

        // Setup location puck unconditionally
        setupLocationPuck()
        startLocationTracking()

        // Load custom map style
        val styleUrl = mapViewModel.getCurrentStyleUrl()
        mapView.mapboxMap.loadStyle(styleUrl) { style ->
            setupMapStyle(style)
            addStationImages(style)
        }
    }

    private fun setupMapStyle(style: Style) {
        // Apply light preset based on theme
        val styleImportId = style.getStyleImports().firstOrNull()?.id
        styleImportId?.let {
            style.setStyleImportConfigProperty(it, "lightPreset", com.mapbox.bindgen.Value.valueOf("day"))
        }
    }

    private fun reloadMapStyle(mapStyleURL: MapStyleURL) {
        mapView.mapboxMap.loadStyle(mapStyleURL.getStyleUrl()) { style ->
            setupMapStyle(style)
            addStationImages(style)
            // Re-render station markers after style reload
            val stations = mapViewModel.stations.value
            if (stations.isNotEmpty()) {
                renderStationMarkers(stations)
            }
        }
    }

    // endregion

    // region Feature 2: Location Puck

    private fun setupLocationPuck() {
        mapView.location.apply {
            updateSettings {
                enabled = true
                pulsingEnabled = true
                puckBearingEnabled = true
                puckBearing = PuckBearing.HEADING
                showAccuracyRing = true
            }
            setLocationProvider(navigationLocationProvider)

            val iconNavigationArrow = ImageHolder.from(R.drawable.ic_navigation_arrow)
            val topImage = ImageHolder.from(R.drawable.ic_navigation_puck_background)
            locationPuck = LocationPuck2D(
                topImage = iconNavigationArrow,
                bearingImage = topImage,
                shadowImage = topImage,
                scaleExpression = literal(1.4).toJson()
            )
        }
    }

    // endregion

    // region Feature 3: Camera Follow + Location Tracking

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        if (permissionHelper?.isLocationPermissionGranted() != true) return

        // Get last known location first for immediate camera update
        locationManager?.getLastKnownLocation { location ->
            location?.let {
                mapViewModel.updateLocation(it)
                navigationLocationProvider.changePosition(it)
                jumpToLocation(it)
            }
        }

        // Start continuous updates
        locationManager?.startLocationUpdates { location ->
            mapViewModel.updateLocation(location)
            navigationLocationProvider.changePosition(location)
            setGofaCamera(location, location.bearing.toDouble())
        }
    }

    private fun jumpToLocation(location: Location) {
        val center = Point.fromLngLat(location.longitude, location.latitude)
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(center)
                .zoom(Constants.DEFAULT_MAP_ZOOM_LEVEL)
                .bearing(location.bearing.toDouble())
                .pitch(if (isPitch) mapView.mapboxMap.cameraState.pitch else 0.0)
                .build()
        )
    }

    private fun flyToLocation(location: Location, duration: Long = FLY_DURATION) {
        val center = Point.fromLngLat(location.longitude, location.latitude)
        mapView.camera.flyTo(
            CameraOptions.Builder()
                .center(center)
                .zoom(Constants.TINY_MAP_ZOOM_LEVEL)
                .bearing(location.bearing.toDouble())
                .pitch(if (isPitch) mapView.mapboxMap.cameraState.pitch else 0.0)
                .build(),
            MapAnimationOptions.Builder()
                .duration(duration)
                .build()
        )
    }

    private fun setGofaCamera(trueLocation: Location, bearing: Double) {
        if (!isFollowCamera) return

        var pitch = mapView.mapboxMap.cameraState.pitch
        var zoom = mapView.mapboxMap.cameraState.zoom

        val currentSpeed = trueLocation.speed // Speed in m/s
        val minSpeedToActionMs = Constants.MIN_USER_SPEED_TO_ACTION / 3.6

        if (currentSpeed > minSpeedToActionMs) {
            pitch = if (!isPitch) Constants.DEFAULT_MAP_PITCH else pitch
            zoom = if (zoom == Constants.TINY_MAP_ZOOM_LEVEL) Constants.DEFAULT_MAP_ZOOM_LEVEL else zoom
        } else {
            pitch = if (!isPitch) Constants.MIN_MAP_PITCH else pitch
            zoom = if (zoom <= Constants.TINY_MAP_ZOOM_LEVEL) Constants.TINY_MAP_ZOOM_LEVEL else zoom
        }

        val point = Point.fromLngLat(trueLocation.longitude, trueLocation.latitude)

        val cameraOptions = if (currentSpeed < minSpeedToActionMs) {
            CameraOptions.Builder()
                .center(point)
                .pitch(pitch)
                .zoom(zoom)
                .build()
        } else {
            CameraOptions.Builder()
                .center(point)
                .pitch(pitch)
                .bearing(bearing)
                .zoom(zoom)
                .build()
        }

        val animationOptions = MapAnimationOptions.Builder()
            .duration(1000)
            .interpolator(android.view.animation.LinearInterpolator())
            .build()

        mapView.camera.easeTo(cameraOptions, animationOptions)
    }

    private fun focusToMyLocation() {
        isPitch = false
        isFollowCamera = true
        mapViewModel.setFollowingCamera(true)
        binding.btnMyLocation.visibility = View.GONE

        val currentLocation = mapViewModel.currentLocation.value
        if (currentLocation != null) {
            flyToLocation(currentLocation)
        } else {
            locationManager?.getLastKnownLocation { location ->
                location?.let {
                    mapViewModel.updateLocation(it)
                    navigationLocationProvider.changePosition(it)
                    flyToLocation(it)
                }
            }
        }
    }

    private fun onCameraTrackingDismissed() {
        isFollowCamera = false
        mapViewModel.setFollowingCamera(false)
    }

    // endregion

    // region Feature 4: Gesture Listeners

    private fun setupGestureListeners() {
        // Move listener — detect user drag
        mapView.gestures.addOnMoveListener(object : OnMoveListener {
            override fun onMoveBegin(detector: MoveGestureDetector) {}

            override fun onMove(detector: MoveGestureDetector): Boolean {
                onCameraTrackingDismissed()
                binding.btnMyLocation.visibility = View.VISIBLE
                return false
            }

            override fun onMoveEnd(detector: MoveGestureDetector) {
                // Refresh station markers when map pans
                if (mapViewModel.stationProvider != StationProvider.NONE) {
                    val center = mapView.mapboxMap.cameraState.center
                    val zoom = mapView.mapboxMap.cameraState.zoom
                    mapViewModel.fetchStations(center.latitude(), center.longitude(), zoom)
                }
            }
        })

        // Click listener — check station markers first, then handle other clicks
        mapView.gestures.addOnMapClickListener { point ->
            handleMapClick(point)
            false
        }

        // Long click listener — drop pin
        mapView.gestures.addOnMapLongClickListener { point ->
            dropPinAtPoint(point)
            true
        }

        // Shove listener — detect pitch changes
        mapView.gestures.addOnShoveListener(object : OnShoveListener {
            override fun onShoveBegin(detector: ShoveGestureDetector) {}
            override fun onShove(detector: ShoveGestureDetector) {
                isPitch = true
            }
            override fun onShoveEnd(detector: ShoveGestureDetector) {}
        })

        // Scale listener — detect zoom changes
        mapView.gestures.addOnScaleListener(object : OnScaleListener {
            override fun onScaleBegin(detector: StandardScaleGestureDetector) {}
            override fun onScale(detector: StandardScaleGestureDetector) {}
            override fun onScaleEnd(detector: StandardScaleGestureDetector) {}
        })
    }

    /**
     * Handle map click: query rendered features on station layer.
     * If a station marker was tapped, show the StationInfoBottomSheet.
     */
    private fun handleMapClick(point: Point) {
        val screenPoint = mapView.mapboxMap.pixelForCoordinate(point)

        mapView.mapboxMap.queryRenderedFeatures(
            RenderedQueryGeometry(screenPoint),
            RenderedQueryOptions(listOf(STATION_LAYER_ID), null)
        ) { expected ->
            expected.value?.let { features ->
                if (features.isEmpty()) return@queryRenderedFeatures

                val feature = features.first().queriedFeature.feature
                val id = feature.getStringProperty("id")
                if (id != null) {
                    val chosenStation =
                        mapViewModel.stations.value.firstOrNull { it.id.toString() == id }
                    if (chosenStation != null) {
                        mapViewModel.chosenStation = chosenStation
                        showStationInfoBottomSheet(chosenStation)
                        return@queryRenderedFeatures
                    }
                }
            }
        }

        // Clear any destination annotations
        pointAnnotationManager?.deleteAll()
    }

    /**
     * Show the StationInfoBottomSheet for the given station.
     */
    private fun showStationInfoBottomSheet(station: StationModel) {
        val bottomSheet = StationInfoBottomSheet.newInstance(
            station = station,
            provider = mapViewModel.stationProvider
        )
        bottomSheet.show(childFragmentManager, "StationInfoBottomSheet")
    }

    private fun dropPinAtPoint(point: Point) {
        // Clear previous pins
        pointAnnotationManager?.deleteAll()

        // Create annotation manager if needed
        if (pointAnnotationManager == null) {
            pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
        }

        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_map_location_pin)
        icon?.let { drawable ->
            val bitmap = com.n3t.mobile.utils.getBitmapFromDrawable(drawable)
            bitmap?.let {
                val options = PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(it)
                pointAnnotationManager?.create(options)
            }
        }

        // Show my location button since we moved away from tracking
        onCameraTrackingDismissed()
        binding.btnMyLocation.visibility = View.VISIBLE
    }

    // endregion

    // region Feature 6: Bottom Sheet Dashboard

    private fun setupBottomSheet() {
        val bottomSheet = binding.bottomSheetDashboard.root
        val bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun setupUI() {
        // Setup PlaceTypeSession widget
        binding.bottomSheetDashboard.widgetSessionPlaceType.setupData()
    }

    // endregion

    // region Feature 7: Search & Routing + App Events

    private fun listenAppEvents() {
        // My Location button
        binding.btnMyLocation.setOnClickListener {
            focusToMyLocation()
        }

        // Search bar → launch SearchActivity
        binding.bottomSheetDashboard.searchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        // Settings button
        binding.bottomSheetDashboard.btnSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        // Map type button → show ChooseMapTypeFragment
        binding.btnMapType.setOnClickListener {
            val tag = "ChooseMapTypeFragment"
            val existing = childFragmentManager.findFragmentByTag(tag) as? BottomSheetDialogFragment
            if (existing != null && existing.isAdded) return@setOnClickListener

            val bottomSheet = ChooseMapTypeFragment()
            bottomSheet.onMapStyleSelected = { mapStyleURL ->
                mapViewModel.setMapStyle(mapStyleURL)
            }
            bottomSheet.show(childFragmentManager, tag)
        }

        // Station buttons — Fuel
        binding.bottomSheetDashboard.widgetSessionPlaceType.binding.btnFuel.setOnClickListener {
            toggleStation(StationProvider.FUEL)
        }

        // Station buttons — Charge
        binding.bottomSheetDashboard.widgetSessionPlaceType.binding.btnCharge.setOnClickListener {
            toggleStation(StationProvider.CHARGE)
        }
    }

    // endregion

    // region Feature 8: Station Display

    private fun addStationImages(style: Style) {
        // Add fuel station icon
        ResourcesCompat.getDrawable(resources, R.drawable.ic_gas_station, null)?.let { drawable ->
            val bitmap = com.n3t.mobile.utils.getBitmapFromDrawable(drawable)
            bitmap?.let {
                if (style.getStyleImage(FUEL_STATION_ICON) != null) {
                    style.removeStyleImage(FUEL_STATION_ICON)
                }
                style.addImage(FUEL_STATION_ICON, it)
            }
        }

        // Add charge station icon
        ResourcesCompat.getDrawable(resources, R.drawable.ic_electric_station, null)?.let { drawable ->
            val bitmap = com.n3t.mobile.utils.getBitmapFromDrawable(drawable)
            bitmap?.let {
                if (style.getStyleImage(CHARGE_STATION_ICON) != null) {
                    style.removeStyleImage(CHARGE_STATION_ICON)
                }
                style.addImage(CHARGE_STATION_ICON, it)
            }
        }
    }

    /**
     * Toggle station type. If same type, toggle off. Otherwise switch and fetch.
     */
    private fun toggleStation(provider: StationProvider) {
        if (mapViewModel.stationProvider == provider) {
            // Toggle off
            removeStationLayer()
            mapViewModel.stationProvider = StationProvider.NONE
            mapViewModel.clearStations()
            return
        }

        mapViewModel.stationProvider = provider

        // Fetch stations from API based on current map center
        val center = mapView.mapboxMap.cameraState.center
        val zoom = mapView.mapboxMap.cameraState.zoom
        mapViewModel.fetchStations(center.latitude(), center.longitude(), zoom, isCompulsory = true)
    }

    /**
     * Render station markers from a list of StationModels.
     * Creates GeoJSON features with "id" property for queryRenderedFeatures lookup.
     */
    private fun renderStationMarkers(stations: List<StationModel>) {
        val style = mapView.mapboxMap.style ?: return
        val provider = mapViewModel.stationProvider
        if (provider == StationProvider.NONE) return

        val iconName = if (provider == StationProvider.CHARGE) CHARGE_STATION_ICON else FUEL_STATION_ICON

        // Build features
        val features = ArrayList<Feature>()
        stations.forEach { station ->
            val lon = station.position?.firstOrNull()
            val lat = station.position?.lastOrNull()
            if (lat != null && lon != null) {
                val feature = Feature.fromGeometry(Point.fromLngLat(lon, lat))
                feature.addStringProperty("id", station.id.toString())
                features.add(feature)
            }
        }

        // Remove old layer/source
        removeStationLayer()

        // Add source
        val source = GeoJsonSource.Builder(STATION_SOURCE_ID)
            .featureCollection(FeatureCollection.fromFeatures(features))
            .build()
        style.addSource(source)

        // Add symbol layer
        val layer = SymbolLayer(STATION_LAYER_ID, STATION_SOURCE_ID)
            .iconAllowOverlap(true)
            .iconIgnorePlacement(true)
            .iconRotationAlignment(IconRotationAlignment.VIEWPORT)
            .iconImage(iconName)
            .iconSize(0.6)
        layer.slot("top")
        style.addLayer(layer)
    }

    private fun removeStationLayer() {
        val style = mapView.mapboxMap.style ?: return
        if (style.styleLayerExists(STATION_LAYER_ID)) {
            style.removeStyleLayer(STATION_LAYER_ID)
        }
        if (style.styleSourceExists(STATION_SOURCE_ID)) {
            style.removeStyleSource(STATION_SOURCE_ID)
        }
    }

    // endregion

    // region Feature 12: Observe ViewModel (Map Style + Stations)

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe map style changes
                launch {
                    mapViewModel.mapStyle.collect { mapStyleURL ->
                        reloadMapStyle(mapStyleURL)
                    }
                }

                // Observe station data changes
                launch {
                    mapViewModel.stations.collect { stations ->
                        if (stations.isNotEmpty()) {
                            renderStationMarkers(stations)
                        } else if (mapViewModel.stationProvider == StationProvider.NONE) {
                            removeStationLayer()
                        }
                    }
                }
            }
        }
    }

    // endregion
}
