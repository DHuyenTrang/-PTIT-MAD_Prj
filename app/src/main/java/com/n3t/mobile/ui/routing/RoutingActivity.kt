package com.n3t.mobile.ui.routing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.n3t.mobile.R
import com.n3t.mobile.data.model.common.MapStyleURL
import com.n3t.mobile.data.model.common.getStyleUrl
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import com.n3t.mobile.databinding.ActivityRoutingBinding
import com.n3t.mobile.ui.navigation.NavigationActivity
import com.n3t.mobile.ui.routing.adapter.ItemRouteDetailAdapter
import com.n3t.mobile.utils.RouteFormatUtils
import com.n3t.mobile.view_model.search.RoutingUiState
import com.n3t.mobile.view_model.search.RoutingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min
import androidx.core.view.isVisible

class RoutingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutingBinding
    private val viewModel: RoutingViewModel by viewModel()

    private lateinit var routeAdapter: ItemRouteDetailAdapter
    private var pointManager: PointAnnotationManager? = null
    private var drawLineUtils: com.n3t.mobile.utils.DrawLineUtils? = null

    private var origin: CoordinateModel? = null
    private var destination: CoordinateModel? = null
    private var destinationName: String = ""
    private var destinationAddress: String = ""

    private var avoidTolls: Boolean = false
    private var avoidHighways: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        origin = intent.readSerializableExtra(EXTRA_ORIGIN)
        destination = intent.readSerializableExtra(EXTRA_DESTINATION)
        destinationName = intent.getStringExtra(EXTRA_DESTINATION_NAME).orEmpty()
        destinationAddress = intent.getStringExtra(EXTRA_DESTINATION_ADDRESS).orEmpty()

        if (origin == null || destination == null) {
            finish()
            return
        }

        setupMap()
        setupViews()
        observeViewModel()
        loadRoutes()
    }

    private fun setupMap() {
        binding.mapView.mapboxMap.loadStyle(MapStyleURL.DEFAULT.getStyleUrl()) {
            pointManager = binding.mapView.annotations.createPointAnnotationManager()
            drawLineUtils = com.n3t.mobile.utils.DrawLineUtils(binding.mapView.mapboxMap)
            renderEndpoints()
        }
    }

    private fun setupViews() {
        routeAdapter = ItemRouteDetailAdapter { route ->
            viewModel.selectRoute(route.id)
        }

        binding.bottomSheetRoute.rvRoutingDetail.apply {
            layoutManager = LinearLayoutManager(this@RoutingActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = routeAdapter
        }

        binding.bottomSheetRoute.txtMyLocation.text = getString(R.string.current_location)
        binding.bottomSheetRoute.txtDestination.text = destinationName.ifBlank {
            destinationAddress.ifBlank { getString(R.string.search_place) }
        }
        binding.bottomSheetRoute.btnAddDestination.visibility = View.GONE
        binding.bottomSheetRoute.btnSwap.visibility = View.GONE

        binding.bottomSheetRoute.btnClose.setOnClickListener { finish() }
        binding.bottomSheetRoute.btnStart.setOnClickListener {
            val selectedRoute = viewModel.uiState.value.selectedRoute ?: return@setOnClickListener
            val from = origin ?: return@setOnClickListener
            val to = destination ?: return@setOnClickListener
            startActivity(
                NavigationActivity.newIntent(
                    context = this,
                    origin = from,
                    destination = to,
                    destinationName = destinationName,
                    destinationAddress = destinationAddress,
                    selectedRoute = selectedRoute,
                )
            )
        }

        binding.ivEditAndClose.setOnClickListener {
            val expanded = binding.llAvoidRoutes.isVisible
            binding.llAvoidRoutes.visibility = if (expanded) View.GONE else View.VISIBLE
            binding.ivEditAndClose.setImageResource(
                R.drawable.ic_down_arrow
            )
        }

        binding.llAvoidTolls.setOnClickListener {
            avoidTolls = !avoidTolls
            renderAvoidOptionState()
            loadRoutes()
        }

        binding.llAvoidHighways.setOnClickListener {
            avoidHighways = !avoidHighways
            renderAvoidOptionState()
            loadRoutes()
        }

        renderAvoidOptionState()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun loadRoutes() {
        val from = origin ?: return
        val to = destination ?: return
        viewModel.loadRoutes(
            origin = from,
            destination = to,
            avoidTolls = avoidTolls,
            avoidHighways = avoidHighways,
        )
    }

    private fun renderState(state: RoutingUiState) {
        routeAdapter.submitList(state.routes)
        binding.bottomSheetRoute.btnStart.isEnabled = !state.isLoading && state.selectedRoute != null

        if (state.routes.isNotEmpty()) {
            drawRoutes(state.routes)
            focusCameraForAllRoutes(state.routes)
        }
    }

    private fun drawRoutes(routes: List<RouteOptionUiModel>) {
        val utils = drawLineUtils ?: return
        
        // Put the selected route at the end of the list so it draws on top
        val sortedRoutes = routes.sortedBy { it.isSelected }
        
        val routeLines = sortedRoutes.mapNotNull { route ->
            val points = RouteFormatUtils.decodePolyline(route.encodedPolyline)
            if (points.size < 2) return@mapNotNull null
            com.n3t.mobile.utils.DrawLineUtils.RouteLineData(
                points = points,
                isAlternative = !route.isSelected
            )
        }
        
        utils.updateRoutes(routeLines)
    }

    private fun renderEndpoints() {
        val manager = pointManager ?: return
        val from = origin ?: return
        val to = destination ?: return

        manager.deleteAll()
        manager.create(PointAnnotationOptions().withPoint(Point.fromLngLat(from.longitude, from.latitude)))
        manager.create(PointAnnotationOptions().withPoint(Point.fromLngLat(to.longitude, to.latitude)))
    }

    private fun focusCameraForAllRoutes(routes: List<RouteOptionUiModel>) {
        val allPoints = routes.flatMap { RouteFormatUtils.decodePolyline(it.encodedPolyline) }
        if (allPoints.isEmpty()) return

        val padding = com.mapbox.maps.EdgeInsets(120.0, 100.0, 80.0, 100.0)
        val cameraOptions = binding.mapView.mapboxMap.cameraForCoordinates(
            allPoints,
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

    private fun renderAvoidOptionState() {
        updateAvoidOption(binding.llAvoidTolls, avoidTolls)
        updateAvoidOption(binding.llAvoidHighways, avoidHighways)
    }

    private fun updateAvoidOption(view: View, isActive: Boolean) {
        view.setBackgroundResource(if (isActive) R.drawable.bg_route_card_selected else R.drawable.bg_address_category)
        val textView = (view as? android.view.ViewGroup)?.getChildAt(0) as? TextView
        val color = if (isActive) R.color.primary else R.color.onSurfaceVariant
        textView?.setTextColor(ContextCompat.getColor(this, color))
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
        pointManager = null
        drawLineUtils = null
        super.onDestroy()
    }

    companion object {
        private const val EXTRA_ORIGIN = "extra_origin"
        private const val EXTRA_DESTINATION = "extra_destination"
        private const val EXTRA_DESTINATION_NAME = "extra_destination_name"
        private const val EXTRA_DESTINATION_ADDRESS = "extra_destination_address"

        fun newIntent(
            context: Context,
            origin: CoordinateModel,
            destination: CoordinateModel,
            destinationName: String,
            destinationAddress: String,
        ): Intent {
            return Intent(context, RoutingActivity::class.java).apply {
                putExtra(EXTRA_ORIGIN, origin)
                putExtra(EXTRA_DESTINATION, destination)
                putExtra(EXTRA_DESTINATION_NAME, destinationName)
                putExtra(EXTRA_DESTINATION_ADDRESS, destinationAddress)
            }
        }
    }
}
