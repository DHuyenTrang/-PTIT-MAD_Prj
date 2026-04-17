package com.n3t.mobile.core.navigation

import android.location.Location
import com.mapbox.geojson.Point
import com.n3t.mobile.data.model.goong.directions.GoongRoute
import com.n3t.mobile.utils.RouteFormatUtils
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class RouteProgressSnapshot(
    val routePoints: List<Point>,
    val remainingPoints: List<Point>,
    val progressPercent: Float,
    val remainingDistanceMeters: Double,
    val remainingDurationSeconds: Int,
    val currentInstruction: String,
    val roadName: String,
    val stepIndex: Int,
    val isOffRoute: Boolean,
    // ── Maneuver data for gofa-style navigation instructions ──
    val upcomingStepName: String = "",
    val upcomingModifier: String = "",
    val upcomingType: String = "",
    val stepDistanceRemaining: Double = 0.0,
)

object RouteProgressCalculator {

    private const val OFF_ROUTE_DISTANCE_METERS = 75.0

    fun buildSnapshot(route: GoongRoute, location: Location): RouteProgressSnapshot {
        val routePoints = route.overviewPolyline?.points
            ?.let(RouteFormatUtils::decodePolyline)
            .orEmpty()
            
        val totalDistance = route.legs?.sumOf { it.distance?.value ?: 0 }?.toDouble() ?: 0.0
        val totalDuration = route.legs?.sumOf { it.duration?.value ?: 0 } ?: 0

        if (routePoints.size < 2) {
            return buildFallbackSnapshot(route, routePoints, totalDistance, totalDuration)
        }

        val projection = projectLocation(routePoints, location)
        val routeDistanceMeters = max(totalDistance, projection.totalDistanceMeters)
        val remainingDistanceMeters = routeDistanceMeters - projection.distanceTraveledMeters
        val progressPercent = if (routeDistanceMeters <= 0.0) {
            0f
        } else {
            (projection.distanceTraveledMeters / routeDistanceMeters).toFloat().coerceIn(0f, 1f)
        }
        val routeDurationSeconds = totalDuration.takeIf { it > 0 } ?: (routeDistanceMeters / max(projection.averageSpeedMetersPerSecond, 1.0)).roundToInt()
        val remainingDurationSeconds = if (routeDistanceMeters <= 0.0) {
            0
        } else {
            (routeDurationSeconds * (1.0 - progressPercent)).roundToInt()
        }

        val stepCandidate = findNearestStep(route, location)
        val instruction = stepCandidate?.instruction ?: "Đang dẫn đường"
        val roadName = stepCandidate?.roadName ?: route.summary.orEmpty()

        return RouteProgressSnapshot(
            routePoints = routePoints,
            remainingPoints = projection.remainingPoints,
            progressPercent = progressPercent,
            remainingDistanceMeters = remainingDistanceMeters.coerceAtLeast(0.0),
            remainingDurationSeconds = remainingDurationSeconds.coerceAtLeast(0),
            currentInstruction = instruction,
            roadName = roadName,
            stepIndex = stepCandidate?.index ?: 0,
            isOffRoute = projection.nearestDistanceMeters > OFF_ROUTE_DISTANCE_METERS,
            upcomingStepName = stepCandidate?.upcomingStepName.orEmpty(),
            upcomingModifier = stepCandidate?.upcomingModifier.orEmpty(),
            upcomingType = stepCandidate?.upcomingType.orEmpty(),
            stepDistanceRemaining = stepCandidate?.stepDistanceRemaining ?: 0.0,
        )
    }

    private fun buildFallbackSnapshot(
        route: GoongRoute,
        routePoints: List<Point>,
        totalDistance: Double,
        totalDuration: Int,
    ): RouteProgressSnapshot {
        return RouteProgressSnapshot(
            routePoints = routePoints,
            remainingPoints = routePoints,
            progressPercent = 0f,
            remainingDistanceMeters = totalDistance,
            remainingDurationSeconds = totalDuration,
            currentInstruction = route.summary ?: "Đang dẫn đường",
            roadName = route.summary.orEmpty(),
            stepIndex = 0,
            isOffRoute = false,
        )
    }

    private data class ProjectionResult(
        val projectedPoint: Point,
        val nearestDistanceMeters: Double,
        val distanceTraveledMeters: Double,
        val totalDistanceMeters: Double,
        val remainingPoints: List<Point>,
        val averageSpeedMetersPerSecond: Double,
    )

    private data class StepCandidate(
        val index: Int,
        val instruction: String,
        val roadName: String,
        val distanceMeters: Double,
        val upcomingStepName: String = "",
        val upcomingModifier: String = "",
        val upcomingType: String = "",
        val stepDistanceRemaining: Double = 0.0,
    )

    private data class LocationVector(
        val x: Double,
        val y: Double,
    )

    private fun projectLocation(routePoints: List<Point>, location: Location): ProjectionResult {
        val locationPoint = Point.fromLngLat(location.longitude, location.latitude)
        var bestDistance = Double.MAX_VALUE
        var bestProjection = routePoints.first()
        var bestTraveled = 0.0
        var cumulativeDistance = 0.0
        var bestSegmentIndex = 0

        for (index in 0 until routePoints.lastIndex) {
            val start = routePoints[index]
            val end = routePoints[index + 1]
            val segmentLength = haversineMeters(start, end)
            val projection = projectOnSegment(locationPoint, start, end)
            if (projection.distanceMeters < bestDistance) {
                bestDistance = projection.distanceMeters
                bestProjection = projection.point
                bestTraveled = cumulativeDistance + (segmentLength * projection.fraction)
                bestSegmentIndex = index
            }
            cumulativeDistance += segmentLength
        }

        val remainingPoints = buildList {
            add(bestProjection)
            for (index in bestSegmentIndex + 1 until routePoints.size) {
                add(routePoints[index])
            }
        }

        val averageSpeed = if (location.speed > 0f) {
            location.speed.toDouble().coerceAtLeast(1.0)
        } else {
            12.0
        }

        return ProjectionResult(
            projectedPoint = bestProjection,
            nearestDistanceMeters = bestDistance,
            distanceTraveledMeters = bestTraveled,
            totalDistanceMeters = cumulativeDistance,
            remainingPoints = remainingPoints,
            averageSpeedMetersPerSecond = averageSpeed,
        )
    }

    private fun findNearestStep(route: GoongRoute, location: Location): StepCandidate? {
        val steps = route.legs?.flatMap { it.steps.orEmpty() }.orEmpty()
        if (steps.isEmpty()) return null

        var bestCandidate: StepCandidate? = null
        var bestIndex: Int = 0
        steps.forEachIndexed { index, step ->
            val stepPoints = step.polyline?.points
                ?.let(RouteFormatUtils::decodePolyline)
                .orEmpty()
            if (stepPoints.size < 2) return@forEachIndexed

            val distance = distanceToPolyline(location, stepPoints)
            val instruction = step.htmlInstructions?.replace(Regex("<.*?>"), " ")?.trim()
                ?: step.maneuver
                ?: "Đang dẫn đường"
            val roadName = instruction

            val candidate = StepCandidate(
                index = index,
                instruction = instruction,
                roadName = roadName,
                distanceMeters = distance,
            )
            if (bestCandidate == null || distance < bestCandidate!!.distanceMeters) {
                bestCandidate = candidate
                bestIndex = index
            }
        }

        // ── Extract upcoming step maneuver data ──
        val upcomingIndex = bestIndex + 1
        val upcomingStep = if (upcomingIndex < steps.size) steps[upcomingIndex] else null
        val upcomingManeuver = upcomingStep?.maneuver.orEmpty()
        // Goong maneuver format: "turn-left", "turn-right", "straight", etc.
        val upcomingModifier = mapGoongManeuverToModifier(upcomingManeuver)
        val upcomingType = mapGoongManeuverToType(upcomingManeuver)
        val upcomingStepName = upcomingStep?.htmlInstructions
            ?.replace(Regex("<.*?>"), " ")?.trim().orEmpty()
        val stepDistanceRemaining = bestCandidate?.distanceMeters ?: 0.0

        return bestCandidate?.copy(
            upcomingStepName = upcomingStepName,
            upcomingModifier = upcomingModifier,
            upcomingType = upcomingType,
            stepDistanceRemaining = stepDistanceRemaining,
        )
    }

    /**
     * Maps Goong maneuver strings like "turn-left", "turn-sharp-right", "roundabout-left"
     * to ModifierMap-compatible modifier strings like "left", "sharp right".
     */
    private fun mapGoongManeuverToModifier(maneuver: String): String {
        return when {
            maneuver.contains("uturn") -> "uturn"
            maneuver.contains("sharp-right") || maneuver.contains("sharp right") -> "sharp right"
            maneuver.contains("sharp-left") || maneuver.contains("sharp left") -> "sharp left"
            maneuver.contains("slight-right") || maneuver.contains("slight right") -> "slight right"
            maneuver.contains("slight-left") || maneuver.contains("slight left") -> "slight left"
            maneuver.contains("right") -> "right"
            maneuver.contains("left") -> "left"
            maneuver.contains("straight") || maneuver.contains("merge") || maneuver.contains("depart") -> ""
            else -> ""
        }
    }

    /**
     * Maps Goong maneuver to a type string compatible with gofa logic.
     */
    private fun mapGoongManeuverToType(maneuver: String): String {
        return when {
            maneuver.contains("roundabout") -> "roundabout"
            maneuver.contains("exit") -> "exit roundabout"
            else -> "turn"
        }
    }

    private data class SegmentProjection(
        val point: Point,
        val fraction: Double,
        val distanceMeters: Double,
    )

    private fun projectOnSegment(point: Point, start: Point, end: Point): SegmentProjection {
        val referenceLatitude = (start.latitude() + end.latitude()) / 2.0
        val startVector = toVector(start, referenceLatitude)
        val endVector = toVector(end, referenceLatitude)
        val pointVector = toVector(point, referenceLatitude)

        val segmentX = endVector.x - startVector.x
        val segmentY = endVector.y - startVector.y
        val pointX = pointVector.x - startVector.x
        val pointY = pointVector.y - startVector.y
        val segmentLengthSquared = segmentX * segmentX + segmentY * segmentY
        val rawFraction = if (segmentLengthSquared <= 0.0) 0.0 else (pointX * segmentX + pointY * segmentY) / segmentLengthSquared
        val fraction = rawFraction.coerceIn(0.0, 1.0)

        val projectedX = startVector.x + (segmentX * fraction)
        val projectedY = startVector.y + (segmentY * fraction)
        val projectedPoint = fromVector(projectedX, projectedY, referenceLatitude)

        return SegmentProjection(
            point = projectedPoint,
            fraction = fraction,
            distanceMeters = haversineMeters(point, projectedPoint),
        )
    }

    private fun distanceToPolyline(point: Location, polyline: List<Point>): Double {
        var bestDistance = Double.MAX_VALUE
        val locationPoint = Point.fromLngLat(point.longitude, point.latitude)
        for (index in 0 until polyline.lastIndex) {
            val projection = projectOnSegment(locationPoint, polyline[index], polyline[index + 1])
            bestDistance = min(bestDistance, projection.distanceMeters)
        }
        return bestDistance
    }

    private fun toVector(point: Point, referenceLatitude: Double): LocationVector {
        val radius = 6_371_000.0
        val x = Math.toRadians(point.longitude()) * cos(Math.toRadians(referenceLatitude)) * radius
        val y = Math.toRadians(point.latitude()) * radius
        return LocationVector(x, y)
    }

    private fun fromVector(x: Double, y: Double, referenceLatitude: Double): Point {
        val radius = 6_371_000.0
        val latitude = Math.toDegrees(y / radius)
        val longitude = Math.toDegrees(x / (radius * cos(Math.toRadians(referenceLatitude))))
        return Point.fromLngLat(longitude, latitude)
    }

    private fun haversineMeters(start: Point, end: Point): Double {
        val radius = 6_371_000.0
        val lat1 = Math.toRadians(start.latitude())
        val lat2 = Math.toRadians(end.latitude())
        val deltaLat = lat2 - lat1
        val deltaLng = Math.toRadians(end.longitude() - start.longitude())
        val a = sin(deltaLat / 2).let { it * it } + cos(lat1) * cos(lat2) * sin(deltaLng / 2).let { it * it }
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun parseDurationSeconds(value: String?): Int {
        if (value.isNullOrBlank()) return 0
        val digits = Regex("\\d+").find(value)?.value?.toLongOrNull() ?: return 0
        return digits.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }
}