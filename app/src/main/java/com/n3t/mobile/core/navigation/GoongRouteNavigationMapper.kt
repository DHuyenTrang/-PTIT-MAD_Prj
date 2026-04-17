package com.n3t.mobile.core.navigation

import com.mapbox.geojson.Point
import com.mapbox.services.android.navigation.v5.models.DirectionsRoute
import com.mapbox.services.android.navigation.v5.models.LegStep
import com.mapbox.services.android.navigation.v5.models.ManeuverModifier
import com.mapbox.services.android.navigation.v5.models.RouteLeg
import com.mapbox.services.android.navigation.v5.models.StepIntersection
import com.mapbox.services.android.navigation.v5.models.StepManeuver
import com.n3t.mobile.data.model.goong.directions.GoongRoute
import com.n3t.mobile.data.model.goong.directions.GoongStep

object GoongRouteNavigationMapper {

    fun toDirectionsRoute(route: GoongRoute, routeIndex: Int = 0): DirectionsRoute {
        val legs = route.legs.orEmpty()
        val totalDistance = legs.sumOf { it.distance?.value ?: 0 }
        val totalDuration = legs.sumOf { it.duration?.value ?: 0 }

        val mapboxLegs = legs.mapIndexed { legIndex, leg ->
            RouteLeg.builder()
                .distance((leg.distance?.value ?: 0).toDouble())
                .duration((leg.duration?.value ?: 0).toDouble())
                .summary(route.summary ?: "")
                .steps(leg.steps.orEmpty().mapIndexed { stepIndex, step ->
                    buildStep(step, stepIndex, leg.steps?.size ?: 0)
                })
                .build()
        }

        return DirectionsRoute.builder()
            .distance(totalDistance.toDouble())
            .duration(totalDuration.toDouble())
            .durationTypical(totalDuration.toDouble())
            .geometry(route.overviewPolyline?.points)
            .legs(mapboxLegs)
            .weight(totalDistance.toDouble())
            .weightName("routability")
            .build()
    }

    private fun buildStep(step: GoongStep, stepIndex: Int, stepCount: Int): LegStep {
        val geometry = step.polyline?.points.orEmpty()
        val points = com.n3t.mobile.utils.RouteFormatUtils.decodePolyline(geometry)
        val startPoint = points.firstOrNull() ?: Point.fromLngLat(0.0, 0.0)
        val endPoint = points.lastOrNull() ?: startPoint
        val maneuverType = when {
            stepIndex == 0 -> StepManeuver.DEPART
            stepIndex == stepCount - 1 -> StepManeuver.ARRIVE
            else -> StepManeuver.TURN
        }
        val bearingBefore = points.getOrNull(points.size - 2)?.let { bearingBetween(it, endPoint) }
        val bearingAfter = points.getOrNull(1)?.let { bearingBetween(startPoint, it) }
        
        val instruction = step.htmlInstructions?.replace(Regex("<.*?>"), " ")?.trim() ?: step.maneuver ?: "Đang dẫn đường"

        return LegStep.builder()
            .distance((step.distance?.value ?: 0).toDouble())
            .duration((step.duration?.value ?: 0).toDouble())
            .durationTypical((step.duration?.value ?: 0).toDouble())
            .geometry(geometry)
            .name(instruction)
            .mode("driving")
            .maneuver(
                StepManeuver.builder()
                    .rawLocation(doubleArrayOf(startPoint.longitude(), startPoint.latitude()))
                    .bearingBefore(bearingBefore)
                    .bearingAfter(bearingAfter)
                    .instruction(instruction)
                    .type(maneuverType)
                    .modifier(detectModifier(instruction, step.maneuver))
                    .build()
            )
            .voiceInstructions(emptyList())
            .bannerInstructions(emptyList())
            .drivingSide("right")
            .weight((step.distance?.value ?: 0).toDouble())
            .intersections(
                listOf(
                    StepIntersection.builder()
                        .rawLocation(doubleArrayOf(startPoint.longitude(), startPoint.latitude()))
                        .bearings(listOf(0))
                        .entry(listOf(true))
                        .`in`(0)
                        .out(0)
                        .build()
                )
            )
            .build()
    }

    private fun bearingBetween(start: Point, end: Point): Double {
        val startLat = Math.toRadians(start.latitude())
        val endLat = Math.toRadians(end.latitude())
        val deltaLon = Math.toRadians(end.longitude() - start.longitude())
        val y = kotlin.math.sin(deltaLon) * kotlin.math.cos(endLat)
        val x = kotlin.math.cos(startLat) * kotlin.math.sin(endLat) -
            kotlin.math.sin(startLat) * kotlin.math.cos(endLat) * kotlin.math.cos(deltaLon)
        return (Math.toDegrees(kotlin.math.atan2(y, x)) + 360.0) % 360.0
    }

    private fun detectModifier(instruction: String, maneuver: String?): String? {
        val normalized = instruction.lowercase() + " " + maneuver?.lowercase()
        return when {
            normalized.contains("u-turn") || normalized.contains("uturn") || normalized.contains("quay đầu") -> ManeuverModifier.UTURN
            normalized.contains("slight left") || normalized.contains("chếch sang trái") -> ManeuverModifier.SLIGHT_LEFT
            normalized.contains("sharp left") || normalized.contains("ngoặt trái") -> ManeuverModifier.SHARP_LEFT
            normalized.contains("left") || normalized.contains("rẽ trái") -> ManeuverModifier.LEFT
            normalized.contains("slight right") || normalized.contains("chếch sang phải") -> ManeuverModifier.SLIGHT_RIGHT
            normalized.contains("sharp right") || normalized.contains("ngoặt phải") -> ManeuverModifier.SHARP_RIGHT
            normalized.contains("right") || normalized.contains("rẽ phải") -> ManeuverModifier.RIGHT
            normalized.contains("straight") || normalized.contains("đi thẳng") -> ManeuverModifier.STRAIGHT
            else -> null
        }
    }
}
