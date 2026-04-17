package com.n3t.mobile.ui.navigation

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.WindowMetricsCalculator
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.removeOnMoveListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.services.android.navigation.v5.location.engine.LocationEngine
import com.mapbox.services.android.navigation.v5.location.engine.LocationEngineProvider
import com.mapbox.services.android.navigation.v5.models.DirectionsRoute
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions
import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress
import com.n3t.mobile.R
import com.n3t.mobile.core.mapbox.navigation.NavigationLocationProvider
import com.n3t.mobile.core.navigation.GoongRouteNavigationMapper
import com.n3t.mobile.data.model.common.MapStyleURL
import com.n3t.mobile.data.model.common.getStyleUrl
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import com.n3t.mobile.databinding.ActivityNavigationBinding
import com.n3t.mobile.utils.DrawLineUtils
import com.n3t.mobile.utils.NavigationConstants
import com.n3t.mobile.utils.RouteFormatUtils
import com.n3t.mobile.utils.MapUltis.CameraProvider
import com.n3t.mobile.utils.extensions.copyWith
import com.n3t.mobile.utils.location.LocationPermissionHelper
import com.n3t.mobile.utils.location.MyLocationListener
import com.n3t.mobile.utils.location.MyLocationManager
import com.n3t.mobile.view_model.navigation.NavigationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import kotlinx.coroutines.launch

class NavigationActivity : AppCompatActivity(),
    LocationPermissionHelper.PermissionListener,
    ProgressChangeListener,
    OffRouteListener,
    NavigationEventListener {

    private lateinit var binding: ActivityNavigationBinding
    private val viewModel: NavigationViewModel by viewModel()

    private var pointManager: PointAnnotationManager? = null
    private var drawLineUtils: DrawLineUtils? = null
    private var locationManager: MyLocationManager? = null
    private var permissionHelper: LocationPermissionHelper? = null
    private var navigationLocationProvider = NavigationLocationProvider()
    private var locationEngine: LocationEngine? = null
    private var mapboxNavigation: MapboxNavigation? = null
    private var activeDirectionsRoute: DirectionsRoute? = null
    private var isNavigationStarted = false

    private var origin: CoordinateModel? = null
    private var destination: CoordinateModel? = null
    private var selectedRoute: RouteOptionUiModel? = null
    private var destinationName: String = ""
    private var destinationAddress: String = ""
    private var currentLocationPoint: Point? = null
    private var _currentLocation: android.location.Location? = null

    // ── Camera tracking state (ported from gofa) ──────────────────────
    private var cameraPadding: EdgeInsets = EdgeInsets(0.0, 0.0, 0.0, 0.0)
    private var isFollowCamera: Boolean = true
    private var isPitch: Boolean = false
    private var isZoom: Boolean = false
    private var isShowBtnMyLocation: Boolean = true
    private var needShowRecenterButtonWhenSpeedDown: Boolean = false
    private var isOverviewRoute: Boolean = false
    private var isProcessingOffRoute: Boolean = false
    private var totalTraveled: Double = 0.0
    private val transitionOptions: (ValueAnimator.() -> Unit) = { duration = 1500 }

    private var currentRouteProgress: RouteProgress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        origin = intent.readSerializableExtra(EXTRA_ORIGIN)
        destination = intent.readSerializableExtra(EXTRA_DESTINATION)
        selectedRoute = intent.readSerializableExtra(EXTRA_SELECTED_ROUTE)
        destinationName = intent.getStringExtra(EXTRA_DESTINATION_NAME).orEmpty()
        destinationAddress = intent.getStringExtra(EXTRA_DESTINATION_ADDRESS).orEmpty()

        locationManager = MyLocationManager(this)
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        mapboxNavigation = MapboxNavigation(this, MapboxNavigationOptions.builder().build())
        mapboxNavigation?.setLocationEngine(locationEngine!!)
        mapboxNavigation?.addProgressChangeListener(this)
        mapboxNavigation?.addOffRouteListener(this)
        mapboxNavigation?.addNavigationEventListener(this)

        if (origin == null || destination == null || selectedRoute == null) {
            finish()
            return
        }

        isPitch = false
        isZoom = false

        setupMap()
        setupSummary()
        setupActions()
        observeNavigationState()
        observeCameraState()

        selectedRoute?.let {
            viewModel.startNavigation(it, destinationName, destinationAddress)
        }

        permissionHelper = LocationPermissionHelper(this)

        // Setup puck with NavigationLocationProvider
        binding.mapView.location.apply {
            updateSettings {
                enabled = true
                pulsingEnabled = true
                puckBearingEnabled = true
                puckBearing = PuckBearing.COURSE
                showAccuracyRing = false
            }
            setLocationProvider(navigationLocationProvider)
            locationPuck = LocationPuck2D(
                topImage = ImageHolder.from(R.drawable.ic_navigation_arrow),
                bearingImage = ImageHolder.from(R.drawable.ic_navigation_puck_background),
                shadowImage = ImageHolder.from(R.drawable.ic_navigation_puck_background),
            )
        }
        pointManager = binding.mapView.annotations.createPointAnnotationManager()

        drawEndpoints()
        setupNavigationSession()
        permissionHelper?.checkLocationPermission(this)
    }

    override fun onStop() {
        stopLocationTracking()
        super.onStop()
    }

    private fun setupMap() {
        binding.mapView.mapboxMap.loadStyle(MapStyleURL.DEFAULT.getStyleUrl()) {
            drawLineUtils = DrawLineUtils(binding.mapView.mapboxMap)
            drawSelectedRoute()
            focusSelectedRoute()
            setupGestureListeners()
        }
    }

    private fun setupSummary() {
        val route = selectedRoute ?: return
        binding.txtRoadName.text = destinationName.ifBlank { destinationAddress }
        binding.txtInstruction.text = "${route.durationText} (${route.distanceText})"
        binding.txtDurationString.text = route.durationText
        binding.txtDistanceString.text = route.distanceText
        binding.txtTime.text = "--:--"
        binding.slider.valueTo = 100f
        binding.slider.value = 0f
    }

    private fun setupActions() {
        binding.btnClose.setOnClickListener {
            finishNavigation()
        }
        binding.btnShowRoute.setOnClickListener {
            showOverViewRoute()
            isOverviewRoute = true
        }
        binding.btnReCenter.setOnClickListener {
            focusToMyLocation()
            isShowBtnMyLocation = false
            binding.btnClose.visibility = View.VISIBLE
            binding.btnReCenter.visibility = View.GONE
            binding.btnShowRoute.visibility = View.GONE
            isOverviewRoute = false
        }
    }

    /**
     * Observe ViewModel UI state for instruction/distance/ETA/maneuver updates.
     */
    private fun observeNavigationState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.txtRoadName.text = state.primaryTitle.ifBlank { destinationName.ifBlank { destinationAddress } }
                    binding.txtInstruction.text = state.instructionText.ifBlank { "Đang dẫn đường" }
                    binding.txtDurationString.text = state.durationText.ifBlank { selectedRoute?.durationText.orEmpty() }
                    binding.txtDistanceString.text = state.distanceText.ifBlank { selectedRoute?.distanceText.orEmpty() }
                    binding.txtTime.text = state.etaText.ifBlank { "--:--" }

                    // Update maneuver icon
                    binding.imgDirection.setImageResource(state.maneuverIconRes)

                    // Update road name
                    if (state.nextRoadName.isNotEmpty()) {
                        binding.txtRoadName.text = state.nextRoadName
                        binding.txtRoadName.visibility = View.VISIBLE
                    }

                    // Update progress slider
                    val sliderValue = (state.progressPercent * 100f).coerceIn(0f, 100f)
                    binding.slider.value = sliderValue

                    if (state.remainingPoints.isNotEmpty()) {
                        drawLineUtils?.updateRoute(state.remainingPoints)
                    }

                    binding.txtInstruction.setTextColor(
                        ContextCompat.getColor(
                            this@NavigationActivity,
                            if (state.isOffRoute) R.color.error else R.color.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }

    /**
     * Observe camera state flows (zoom/pitch) from ViewModel.
     * When ViewModel computes a new zoom/pitch from progress, animate the camera.
     */
    private fun observeCameraState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationZoomLevel.collect { newZoom ->
                    val newPitch = viewModel.autoPitch.value
                    navigationZoomLevelChanged(newZoom, newPitch)
                }
            }
        }
    }

    /**
     * Called when the ViewModel recalculates the navigation zoom level / pitch.
     * Only applies if user has NOT overridden with gestures.
     */
    private fun navigationZoomLevelChanged(zoomLevel: Double, pitch: Double) {
        if (!isFollowCamera) return

        val currentPoint = currentLocationPoint ?: return
        val cameraBuilder = CameraProvider.Builder()
            .center(currentPoint)

        if (!isZoom) {
            cameraBuilder.zoom(zoomLevel)
        }
        if (!isPitch) {
            cameraBuilder.pitch(pitch)
        }

        val camera = cameraBuilder.build()
        binding.mapView.mapboxMap.easeTo(
            camera.toMapbox(),
            MapAnimationOptions.mapAnimationOptions {
                duration(1200)
                interpolator(LinearInterpolator())
            }
        )
    }

    private fun setupNavigationSession() {
        if (isNavigationStarted) return
        val route = selectedRoute?.rawRoute ?: return
        val directionsRoute = GoongRouteNavigationMapper.toDirectionsRoute(route)
        activeDirectionsRoute = directionsRoute
        mapboxNavigation?.startNavigation(directionsRoute)
        isNavigationStarted = true
    }

    private fun stopNavigationSession() {
        isNavigationStarted = false
        mapboxNavigation?.stopNavigation()
    }

    private fun startLocationTracking() {
        locationManager?.getLastKnownLocation { location ->
            location?.let {
                _currentLocation = it
                currentLocationPoint = Point.fromLngLat(it.longitude, it.latitude)
                viewModel.updateLocation(it)
            }
        }

        locationManager?.startLocationUpdates(
            MyLocationListener { location ->
                _currentLocation = location
                currentLocationPoint = Point.fromLngLat(location.longitude, location.latitude)
                viewModel.updateLocation(location)
            }
        )
    }

    private fun stopLocationTracking() {
        locationManager?.stopLocationUpdates()
    }

    // ── Gesture Listeners (ported from gofa) ─────────────────────────

    private fun setupGestureListeners() {
        binding.mapView.mapboxMap.addOnMoveListener(onMoveListener)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: com.mapbox.android.gestures.MoveGestureDetector) {
            isShowBtnMyLocation = true
        }

        override fun onMove(detector: com.mapbox.android.gestures.MoveGestureDetector): Boolean {
            if (isShowBtnMyLocation) {
                binding.btnClose.visibility = View.GONE
                binding.btnReCenter.visibility = View.VISIBLE
                binding.btnShowRoute.visibility = View.VISIBLE
            }

            val speedKmh = (_currentLocation?.speed ?: 0f) * 3.6f
            if (speedKmh >= NavigationConstants.SPEED_SCALE) {
                needShowRecenterButtonWhenSpeedDown = true
                isFollowCamera = false
                return false
            }

            needShowRecenterButtonWhenSpeedDown = false
            onCameraTrackingDismissed()
            return false
        }

        override fun onMoveEnd(detector: com.mapbox.android.gestures.MoveGestureDetector) {
            if (needShowRecenterButtonWhenSpeedDown) {
                isFollowCamera = true
            }
        }
    }

    private fun onCameraTrackingDismissed() {
        isFollowCamera = false
    }

    // ── Camera & Puck updates (ported from gofa) ─────────────────────

    /**
     * Update camera to follow user, using animated easeTo.
     * Respects isPitch/isZoom gesture overrides.
     */
    private fun setNavigationCamera(trueLocation: android.location.Location, bearing: Double) {
        if (!isFollowCamera) return

        val zoom = binding.mapView.mapboxMap.cameraState.zoom
        val pitch = if (isPitch) {
            binding.mapView.mapboxMap.cameraState.pitch
        } else {
            viewModel.autoPitch.value
        }

        val point = Point.fromLngLat(trueLocation.longitude, trueLocation.latitude)
        val speedKmh = (_currentLocation?.speed ?: 0f) * 3.6f

        val camera = if (totalTraveled >= NavigationConstants.MIN_DISTANCE_TRAVELED_FOR_BEARING
            && speedKmh < NavigationConstants.MIN_USER_SPEED_TO_ACTION
        ) {
            // Low speed – don't update bearing
            CameraProvider.Builder()
                .center(point)
                .zoom(zoom)
                .pitch(pitch)
                .padding(cameraPadding)
                .build()
        } else {
            CameraProvider.Builder()
                .center(point)
                .pitch(pitch)
                .zoom(zoom)
                .bearing(bearing)
                .padding(cameraPadding)
                .build()
        }

        binding.mapView.mapboxMap.easeTo(
            camera.toMapbox(),
            MapAnimationOptions.mapAnimationOptions {
                duration(1000)
                interpolator(LinearInterpolator())
            }
        )
    }

    /**
     * Compute responsive camera padding based on orientation, screen size.
     */
    private fun computeCameraPadding() {
        val height = binding.mapView.height
        val width = binding.mapView.width
        if (height == 0 || width == 0) return

        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this)
        val widthDp = metrics.bounds.width() / resources.displayMetrics.density
        val orientation = resources.configuration.orientation
        val paddingTop = height * 0.8

        cameraPadding = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (widthDp > 840) {
                cameraPadding.copyWith(top = paddingTop, left = width / 5.0)
            } else {
                cameraPadding.copyWith(top = paddingTop, left = width / 4.0)
            }
        } else {
            cameraPadding.copyWith(top = paddingTop)
        }
    }

    /**
     * Re-center camera on current location with animation.
     */
    private fun focusToMyLocation() {
        onCameraTrackingDismissed()
        setupGestureListeners()

        val location = _currentLocation ?: return
        val centerPoint = Point.fromLngLat(location.longitude, location.latitude)

        computeCameraPadding()

        val camera = CameraProvider.Builder()
            .center(centerPoint)
            .padding(cameraPadding)
            .zoom(NavigationConstants.DEFAULT_MAP_ZOOM_LEVEL)
            .pitch(viewModel.autoPitch.value)
            .bearing(location.bearing.toDouble())
            .build()

        binding.mapView.mapboxMap.flyTo(
            camera.toMapbox(),
            MapAnimationOptions.mapAnimationOptions { duration(800) }
        )

        isFollowCamera = true
    }

    /**
     * Show route overview (zoom out to fit entire route).
     */
    private fun showOverViewRoute() {
        val currentLocation = _currentLocation ?: return
        val waypoint = Point.fromLngLat(currentLocation.longitude, currentLocation.latitude)
        val dest = destination ?: return
        val destPoint = Point.fromLngLat(dest.longitude, dest.latitude)

        val padding = EdgeInsets(100.0, 100.0, 100.0, 100.0)
        val cameraOptions = binding.mapView.mapboxMap.cameraForCoordinates(
            listOf(waypoint, destPoint),
            padding,
            null,
            null
        )

        val finalCamera = CameraOptions.Builder()
            .center(cameraOptions.center)
            .padding(padding)
            .zoom(cameraOptions.zoom?.minus(0.2))
            .pitch(0.0)
            .bearing(0.0)
            .build()

        binding.mapView.mapboxMap.easeTo(
            finalCamera,
            MapAnimationOptions.mapAnimationOptions { duration(1000) }
        )
    }

    // ── Route drawing ────────────────────────────────────────────────

    private fun drawSelectedRoute() {
        val route = selectedRoute ?: return
        val points = RouteFormatUtils.decodePolyline(route.encodedPolyline)
        drawLineUtils?.updateRoute(points)
    }

    private fun drawEndpoints() {
        val manager = pointManager ?: return
        val from = origin ?: return
        val to = destination ?: return

        manager.deleteAll()
        manager.create(PointAnnotationOptions().withPoint(Point.fromLngLat(from.longitude, from.latitude)))
        manager.create(PointAnnotationOptions().withPoint(Point.fromLngLat(to.longitude, to.latitude)))
    }

    private fun focusSelectedRoute() {
        val route = selectedRoute ?: return
        val points = RouteFormatUtils.decodePolyline(route.encodedPolyline)
        if (points.isEmpty()) return

        val padding = EdgeInsets(100.0, 100.0, 100.0, 100.0)

        val cameraOptions = binding.mapView.mapboxMap.cameraForCoordinates(
            points,
            padding,
            null,
            null
        )
        val finalCamera = CameraOptions.Builder()
            .center(cameraOptions.center)
            .padding(padding)
            .zoom(cameraOptions.zoom?.minus(0.2))
            .pitch(0.0)
            .bearing(0.0)
            .build()

        binding.mapView.mapboxMap.setCamera(finalCamera)
    }

    // ── Navigation callbacks ─────────────────────────────────────────

    override fun onPermissionGranted() {
        startLocationTracking()
    }

    override fun onPermissionDenied() {
        // Keep the preview visible even without live navigation.
    }

    override fun onRunning(running: Boolean) {
        // no-op
    }

    override fun userOffRoute(location: android.location.Location?) {
        if (isProcessingOffRoute || isOverviewRoute) return

        location?.let {
            _currentLocation = it
            currentLocationPoint = Point.fromLngLat(it.longitude, it.latitude)
            viewModel.updateLocation(it)
            // TODO: Implement off-route re-routing (Phase 4)
            // isProcessingOffRoute = true
            // 1. Call routing API with current location → destination
            // 2. Update route → redraw → restart navigation session
            // 3. isProcessingOffRoute = false
        }
    }

    override fun onProgressChange(location: android.location.Location?, routeProgress: RouteProgress?) {
        if (location == null) return

        _currentLocation = location
        currentLocationPoint = Point.fromLngLat(location.longitude, location.latitude)
        currentRouteProgress = routeProgress
        totalTraveled = routeProgress?.distanceTraveled() ?: 0.0

        viewModel.updateLocation(location)

        // ── Snap-to-route puck update (ported from gofa) ──────────
        var puckLocation: android.location.Location = location
        if (currentRouteProgress != null) {
            val snapLocation = mapboxNavigation?.snapEngine?.getSnappedLocation(
                location, currentRouteProgress
            )
            snapLocation?.let { snapped ->
                val distance = snapped.distanceTo(location)
                puckLocation = if (distance > NavigationConstants.SNAP_DISTANCE_THRESHOLD) {
                    location
                } else {
                    snapped
                }

                // Use maneuver bearing when starting / low distance traveled
                if (totalTraveled < NavigationConstants.MIN_DISTANCE_TRAVELED_FOR_BEARING) {
                    puckLocation.bearing = currentRouteProgress
                        ?.currentLegProgress()?.currentStep()?.maneuver()
                        ?.bearingAfter()?.toFloat() ?: 0.0f
                } else {
                    puckLocation.bearing = location.bearing
                }
            }
        }

        // Update puck position with smooth animation
        navigationLocationProvider.changePosition(
            puckLocation,
            listOf(puckLocation),
            latLngTransitionOptions = transitionOptions,
            bearingTransitionOptions = transitionOptions,
        )

        // Update camera
        computeCameraPadding()
        setNavigationCamera(puckLocation, location.bearing.toDouble())

        // Check arrival
        routeProgress?.let {
            if (it.distanceRemaining() < NavigationConstants.ARRIVAL_DISTANCE_THRESHOLD) {
                finishNavigation()
            }
        }
    }

    // ── Lifecycle & cleanup ──────────────────────────────────────────

    private fun finishNavigation() {
        stopLocationTracking()
        mapboxNavigation?.stopNavigation()
        mapboxNavigation?.removeProgressChangeListener(this)
        mapboxNavigation?.removeOffRouteListener(this)
        mapboxNavigation?.removeNavigationEventListener(this)
        viewModel.stopNavigation()
        finish()
    }

    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> Intent.readSerializableExtra(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializableExtra(key, T::class.java)
        } else {
            getSerializableExtra(key) as? T
        }
    }

    override fun onDestroy() {
        stopLocationTracking()
        binding.mapView.mapboxMap.removeOnMoveListener(onMoveListener)
        mapboxNavigation?.stopNavigation()
        mapboxNavigation?.removeProgressChangeListener(this)
        mapboxNavigation?.removeOffRouteListener(this)
        mapboxNavigation?.removeNavigationEventListener(this)
        mapboxNavigation?.onDestroy()
        mapboxNavigation = null
        pointManager = null
        drawLineUtils = null
        viewModel.stopNavigation()
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_ORIGIN = "extra_origin"
        private const val EXTRA_DESTINATION = "extra_destination"
        private const val EXTRA_SELECTED_ROUTE = "extra_selected_route"
        private const val EXTRA_DESTINATION_NAME = "extra_destination_name"
        private const val EXTRA_DESTINATION_ADDRESS = "extra_destination_address"

        fun newIntent(
            context: Context,
            origin: CoordinateModel,
            destination: CoordinateModel,
            destinationName: String,
            destinationAddress: String,
            selectedRoute: RouteOptionUiModel,
        ): Intent {
            return Intent(context, NavigationActivity::class.java).apply {
                putExtra(EXTRA_ORIGIN, origin)
                putExtra(EXTRA_DESTINATION, destination)
                putExtra(EXTRA_SELECTED_ROUTE, selectedRoute)
                putExtra(EXTRA_DESTINATION_NAME, destinationName)
                putExtra(EXTRA_DESTINATION_ADDRESS, destinationAddress)
            }
        }
    }
}
