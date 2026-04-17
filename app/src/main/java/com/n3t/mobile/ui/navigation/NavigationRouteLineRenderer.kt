package com.n3t.mobile.ui.navigation

import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager

object NavigationRouteLineRenderer {

    private const val SOURCE_ID_REMAINING = "navigation-route-remaining-source"
    private const val LAYER_ID_REMAINING = "navigation-route-remaining-layer"
    private const val SOURCE_ID_COMPLETED = "navigation-route-completed-source"
    private const val LAYER_ID_COMPLETED = "navigation-route-completed-layer"

    fun render(
        mapView: MapView,
        polylineManager: PolylineAnnotationManager?,
        routePoints: List<Point>,
        remainingPoints: List<Point>,
    ) {
        val style = mapView.mapboxMap.style ?: return
        clear(style)

        if (routePoints.size >= 2) {
            style.addSource(
                geoJsonSource(SOURCE_ID_COMPLETED) {
                    featureCollection(
                        FeatureCollection.fromFeature(
                            Feature.fromGeometry(LineString.fromLngLats(routePoints))
                        )
                    )
                }
            )
            style.addLayer(
                lineLayer(LAYER_ID_COMPLETED, SOURCE_ID_COMPLETED) {
                    lineColor("#D6D9DE")
                    lineWidth(6.0)
                    lineCap(LineCap.ROUND)
                    lineJoin(LineJoin.ROUND)
                }
            )
        }

        if (remainingPoints.size >= 2) {
            style.addSource(
                geoJsonSource(SOURCE_ID_REMAINING) {
                    featureCollection(
                        FeatureCollection.fromFeature(
                            Feature.fromGeometry(LineString.fromLngLats(remainingPoints))
                        )
                    )
                }
            )
            style.addLayer(
                lineLayer(LAYER_ID_REMAINING, SOURCE_ID_REMAINING) {
                    lineColor("#1B73F9")
                    lineWidth(8.0)
                    lineCap(LineCap.ROUND)
                    lineJoin(LineJoin.ROUND)
                }
            )
        } else {
            polylineManager?.deleteAll()
        }
    }

    fun clear(mapView: MapView) {
        val style = mapView.mapboxMap.style ?: return
        clear(style)
    }

    private fun clear(style: com.mapbox.maps.Style) {
        if (style.styleLayerExists(LAYER_ID_REMAINING)) {
            style.removeStyleLayer(LAYER_ID_REMAINING)
        }
        if (style.styleLayerExists(LAYER_ID_COMPLETED)) {
            style.removeStyleLayer(LAYER_ID_COMPLETED)
        }
        if (style.styleSourceExists(SOURCE_ID_REMAINING)) {
            style.removeStyleSource(SOURCE_ID_REMAINING)
        }
        if (style.styleSourceExists(SOURCE_ID_COMPLETED)) {
            style.removeStyleSource(SOURCE_ID_COMPLETED)
        }
    }
}