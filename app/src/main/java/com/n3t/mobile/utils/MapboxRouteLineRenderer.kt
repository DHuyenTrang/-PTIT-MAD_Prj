package com.n3t.mobile.utils

import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import kotlin.math.max

object MapboxRouteLineRenderer {

    private const val SOURCE_PREFIX = "google-route-source-"
    private const val LAYER_PREFIX = "google-route-layer-"

    fun renderRoutes(
        mapView: MapView,
        routes: List<RouteOptionUiModel>,
    ) {
        val style = mapView.mapboxMap.style ?: return
        clearRoutes(style)

        routes.forEachIndexed { index, route ->
            val coordinates = RouteFormatUtils.decodePolyline(route.encodedPolyline)
            if (coordinates.size < 2) return@forEachIndexed

            val sourceId = "$SOURCE_PREFIX$index"
            val layerId = "$LAYER_PREFIX$index"
            style.addSource(
                geoJsonSource(sourceId) {
                    featureCollection(
                        FeatureCollection.fromFeature(
                            Feature.fromGeometry(LineString.fromLngLats(coordinates))
                        )
                    )
                }
            )
            style.addLayer(
                lineLayer(layerId, sourceId) {
                    lineColor(if (route.isSelected) "#006F42" else "#8E9192")
                    lineWidth(if (route.isSelected) 8.0 else 5.0)
                    lineCap(LineCap.ROUND)
                    lineJoin(LineJoin.ROUND)
                }
            )
        }

        val selectedCoordinates = routes.firstOrNull { it.isSelected }
            ?.let { RouteFormatUtils.decodePolyline(it.encodedPolyline) }
            .orEmpty()
        moveCameraToRoute(mapView, selectedCoordinates)
    }

    fun clearRoutes(style: Style) {
        for (index in 0..6) {
            val layerId = "$LAYER_PREFIX$index"
            val sourceId = "$SOURCE_PREFIX$index"
            if (style.styleLayerExists(layerId)) {
                style.removeStyleLayer(layerId)
            }
            if (style.styleSourceExists(sourceId)) {
                style.removeStyleSource(sourceId)
            }
        }
    }

    fun moveCameraToRoute(
        mapView: MapView,
        coordinates: List<Point>,
    ) {
        if (coordinates.isEmpty()) return

        val minLatitude = coordinates.minOf { it.latitude() }
        val maxLatitude = coordinates.maxOf { it.latitude() }
        val minLongitude = coordinates.minOf { it.longitude() }
        val maxLongitude = coordinates.maxOf { it.longitude() }
        val latitudeSpan = maxLatitude - minLatitude
        val longitudeSpan = maxLongitude - minLongitude
        val maxSpan = max(latitudeSpan, longitudeSpan)
        val zoom = when {
            maxSpan < 0.005 -> 15.5
            maxSpan < 0.02 -> 14.0
            maxSpan < 0.05 -> 13.0
            maxSpan < 0.1 -> 12.0
            maxSpan < 0.5 -> 10.0
            else -> 8.0
        }

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(
                    Point.fromLngLat(
                        (minLongitude + maxLongitude) / 2.0,
                        (minLatitude + maxLatitude) / 2.0,
                    )
                )
                .zoom(zoom)
                .build()
        )
    }
}
