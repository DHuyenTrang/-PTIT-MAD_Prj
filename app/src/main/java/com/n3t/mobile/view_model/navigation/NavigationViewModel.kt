package com.n3t.mobile.view_model.navigation

import android.location.Location
import androidx.lifecycle.ViewModel
import com.mapbox.geojson.Point
import com.n3t.mobile.core.navigation.RouteProgressCalculator
import com.n3t.mobile.data.model.map.ModifierMap
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import com.n3t.mobile.utils.NavigationConstants
import com.n3t.mobile.utils.RouteFormatUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

data class NavigationUiState(
    val isTracking: Boolean = false,
    val destinationName: String = "",
    val destinationAddress: String = "",
    val instructionText: String = "",
    val roadName: String = "",
    val durationText: String = "",
    val distanceText: String = "",
    val etaText: String = "",
    val progressPercent: Float = 0f,
    val isOffRoute: Boolean = false,
    val routePoints: List<Point> = emptyList(),
    val remainingPoints: List<Point> = emptyList(),
    val selectedRoute: RouteOptionUiModel? = null,
    // ── Maneuver data ported from gofa ──
    val maneuverModifier: ModifierMap = ModifierMap.STRAIGHT,
    val maneuverIconRes: Int = com.n3t.mobile.R.drawable.road_straight,
    val nextRoadName: String = "",
) {
    val primaryTitle: String
        get() = destinationName.ifBlank { destinationAddress }
}

class NavigationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState.asStateFlow()

    // ── Camera-related state flows (observed by Activity) ────────────
    private val _autoPitch = MutableStateFlow(NavigationConstants.DEFAULT_MAP_PITCH)
    val autoPitch: StateFlow<Double> = _autoPitch.asStateFlow()

    private val _navigationZoomLevel = MutableStateFlow(NavigationConstants.DEFAULT_MAP_ZOOM_LEVEL)
    val navigationZoomLevel: StateFlow<Double> = _navigationZoomLevel.asStateFlow()

    private var _navigationStepIndex = -1

    fun startNavigation(
        route: RouteOptionUiModel,
        destinationName: String,
        destinationAddress: String,
    ) {
        _navigationStepIndex = -1
        val baseEta = formatEta(System.currentTimeMillis(), route.durationSeconds)
        val routePoints = route.rawRoute?.overviewPolyline?.points
            ?.let(RouteFormatUtils::decodePolyline)
            .orEmpty()
            .ifEmpty { RouteFormatUtils.decodePolyline(route.encodedPolyline) }

        _uiState.value = NavigationUiState(
            isTracking = true,
            destinationName = destinationName,
            destinationAddress = destinationAddress,
            instructionText = "Đang dẫn đường",
            roadName = route.label,
            durationText = route.durationText,
            distanceText = route.distanceText,
            etaText = baseEta,
            progressPercent = 0f,
            isOffRoute = false,
            routePoints = routePoints,
            remainingPoints = routePoints,
            selectedRoute = route,
        )
    }

    fun updateLocation(location: Location) {
        val currentRoute = _uiState.value.selectedRoute ?: return
        val rawRoute = currentRoute.rawRoute
        val routePoints = rawRoute?.overviewPolyline?.points
            ?.let(RouteFormatUtils::decodePolyline)
            .orEmpty()
            .ifEmpty { RouteFormatUtils.decodePolyline(currentRoute.encodedPolyline) }

        if (routePoints.size < 2 || rawRoute == null) {
            return
        }

        val snapshot = RouteProgressCalculator.buildSnapshot(rawRoute, location)

        // ── Dynamic zoom level based on speed + distanceRemaining ──
        val speedKmh = location.speed * 3.6f // m/s → km/h
        val distanceRemaining = snapshot.remainingDistanceMeters

        val zoomLevel = if (speedKmh <= NavigationConstants.MIN_USER_SPEED_TO_CHECK) {
            when {
                distanceRemaining < 300 -> NavigationConstants.TINY_MAP_ZOOM_LEVEL
                distanceRemaining < 1000 -> NavigationConstants.MEDIUM_MAP_ZOOM_LEVEL
                else -> NavigationConstants.DEFAULT_MAP_ZOOM_LEVEL
            }
        } else {
            when {
                distanceRemaining < 300 -> NavigationConstants.TINY_MAP_ZOOM_LEVEL
                distanceRemaining < 2000 -> NavigationConstants.MEDIUM_MAP_ZOOM_LEVEL
                else -> NavigationConstants.DEFAULT_MAP_ZOOM_LEVEL
            }
        }
        val pitch = if (distanceRemaining < 300) {
            NavigationConstants.MIN_MAP_PITCH
        } else {
            NavigationConstants.DEFAULT_MAP_PITCH
        }

        _autoPitch.value = pitch
        if (zoomLevel != _navigationZoomLevel.value) {
            _navigationZoomLevel.value = zoomLevel
        }

        // ── Build maneuver instruction (ported from gofa RoutingViewModel) ──
        val upcomingRoadName = snapshot.upcomingStepName
        val currentRoadName = snapshot.roadName
        val nextRoadName = upcomingRoadName.ifEmpty { currentRoadName }

        val modifier = ModifierMap.getValueOf(snapshot.upcomingModifier)
        val type = snapshot.upcomingType

        val distLabel = processLabelDistanceRemaining(
            processDistanceRemaining(snapshot.stepDistanceRemaining)
        )

        val instructionLabel = if (type == "roundabout" || type == "exit roundabout") {
            if (type == "roundabout") "vào vòng xuyến" else "thoát khỏi vòng xuyến"
        } else {
            modifier.toInstructionLabel()
        }

        val fullInstruction = if (nextRoadName.isNotEmpty()) {
            "$distLabel$instructionLabel vào $nextRoadName"
        } else {
            "$distLabel$instructionLabel"
        }

        _uiState.update { state ->
            state.copy(
                instructionText = fullInstruction,
                roadName = nextRoadName.ifBlank { state.roadName },
                progressPercent = snapshot.progressPercent,
                isOffRoute = snapshot.isOffRoute,
                routePoints = snapshot.routePoints,
                remainingPoints = snapshot.remainingPoints,
                etaText = formatEta(System.currentTimeMillis(), snapshot.remainingDurationSeconds),
                distanceText = formatDistance(snapshot.remainingDistanceMeters),
                durationText = formatDuration(snapshot.remainingDurationSeconds),
                maneuverModifier = modifier,
                maneuverIconRes = modifier.toDrawableRes(),
                nextRoadName = nextRoadName,
            )
        }
    }

    fun stopNavigation() {
        _uiState.update { it.copy(isTracking = false) }
    }

    // ── Distance formatting (ported from gofa RoutingViewModel) ──

    private fun processDistanceRemaining(distanceMeters: Double): Int {
        val minDis = if (distanceMeters < 100) 10 else 50
        return (distanceMeters / minDis).roundToInt() * minDis
    }

    private fun processLabelDistanceRemaining(distance: Int): String {
        return if (distance >= 1000) {
            String.format(Locale.getDefault(), "%.1fkm ", distance / 1000.0)
        } else {
            "${distance}m "
        }
    }

    private fun formatEta(nowMillis: Long, durationSeconds: Int): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(
            Date(nowMillis + (durationSeconds.coerceAtLeast(0) * 1000L))
        )
    }

    private fun formatDuration(durationSeconds: Int): String {
        val totalMinutes = (durationSeconds / 60).coerceAtLeast(0)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%dh %02dm", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "%d min", minutes)
        }
    }

    private fun formatDistance(distanceMeters: Double): String {
        return if (distanceMeters >= 1000.0) {
            String.format(Locale.getDefault(), "%.1f km", distanceMeters / 1000.0)
        } else {
            String.format(Locale.getDefault(), "%d m", distanceMeters.roundToInt())
        }
    }
}