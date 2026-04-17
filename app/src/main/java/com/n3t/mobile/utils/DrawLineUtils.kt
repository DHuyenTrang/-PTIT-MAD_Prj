package com.n3t.mobile.utils

import android.graphics.Color
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.addLayerAbove
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs

class DrawLineUtils(private val mapboxMap: MapboxMap) {
    companion object {
        const val SOURCE_ROUTE_ID = "source-route"
        const val LAYER_ROUTE_ID = "layer-route"
        const val LAYER_ROUTE_CASING_ID = "layer-route-casing"

        private val defaultRouteColor = Color.parseColor("#4FADF8")
        private val alternativeRouteColor = Color.parseColor("#8E9192")
        private val borderRouteColor = Color.parseColor("#3265CB")
        private val borderAlternativeRouteColor = Color.parseColor("#5C5F5F")
    }

    private fun ensureLayersExist() {
        val style = mapboxMap.style ?: return

        if (!style.styleSourceExists(SOURCE_ROUTE_ID)) {
            style.addSource(geoJsonSource(SOURCE_ROUTE_ID) {
                lineMetrics(true)
            })
        }

        val expressionColor = Expression.match(
            Expression.get("isAlternative"),
            Expression.literal(1.0),
            Expression.color(alternativeRouteColor),
            Expression.color(defaultRouteColor)
        )

        val expressionCasingColor = Expression.match(
            Expression.get("isAlternative"),
            Expression.literal(1.0),
            Expression.color(borderAlternativeRouteColor),
            Expression.color(borderRouteColor)
        )

        if (!style.styleLayerExists(LAYER_ROUTE_CASING_ID)) {
            val casingLayer = lineLayer(LAYER_ROUTE_CASING_ID, SOURCE_ROUTE_ID) {
                lineCap(LineCap.ROUND)
                lineJoin(LineJoin.ROUND)
                lineColor(expressionCasingColor)
                lineWidth(Expression.interpolate {
                    exponential(2.0)
                    zoom()
                    stop { literal(4.0); literal(6.0) }
                    stop { literal(10.0); literal(8.0) }
                    stop { literal(13.0); literal(12.0) }
                    stop { literal(16.0); literal(20.0) }
                    stop { literal(19.0); literal(28.0) }
                    stop { literal(22.0); literal(36.0) }
                })
            }
            style.addLayer(casingLayer)
        }

        if (!style.styleLayerExists(LAYER_ROUTE_ID)) {
            val routeLayer = lineLayer(LAYER_ROUTE_ID, SOURCE_ROUTE_ID) {
                lineCap(LineCap.ROUND)
                lineJoin(LineJoin.ROUND)
                lineColor(expressionColor)
                lineWidth(Expression.interpolate {
                    exponential(2.0)
                    zoom()
                    stop { literal(4.0); literal(4.5) }
                    stop { literal(10.0); literal(6.0) }
                    stop { literal(13.0); literal(9.0) }
                    stop { literal(16.0); literal(15.0) }
                    stop { literal(19.0); literal(21.0) }
                    stop { literal(22.0); literal(27.0) }
                })
            }
            try {
                style.addLayerAbove(routeLayer, LAYER_ROUTE_CASING_ID)
            } catch (e: Exception) {
                style.addLayer(routeLayer)
            }
        }
    }

    data class RouteLineData(
        val points: List<Point>,
        val isAlternative: Boolean
    )

    fun updateRoutes(routes: List<RouteLineData>) {
        val style = mapboxMap.style ?: return
        ensureLayersExist()
        
        val source = style.getSourceAs<GeoJsonSource>(SOURCE_ROUTE_ID)
        source?.let { geoJsonSource ->
            val features = routes.filter { it.points.size >= 2 }.map { route ->
                Feature.fromGeometry(LineString.fromLngLats(route.points)).apply {
                    addNumberProperty("isAlternative", if (route.isAlternative) 1 else 0)
                }
            }
            geoJsonSource.featureCollection(FeatureCollection.fromFeatures(features))
        }
    }

    fun updateRoute(points: List<Point>, isAlternative: Boolean = false) {
        updateRoutes(listOf(RouteLineData(points, isAlternative)))
    }

    fun clearRoutes() {
        val style = mapboxMap.style ?: return
        val source = style.getSourceAs<GeoJsonSource>(SOURCE_ROUTE_ID)
        source?.featureCollection(FeatureCollection.fromFeatures(emptyList()))
    }
}
