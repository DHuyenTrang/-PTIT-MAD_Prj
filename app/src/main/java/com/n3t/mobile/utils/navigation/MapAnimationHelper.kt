package com.n3t.mobile.utils.navigation

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.animation.flyTo
import com.n3t.mobile.utils.MapUltis.CameraProvider

/**
 * Helper for animated camera transitions on Mapbox maps.
 * Ported from gofa-android MapAnimationOptionsProvider.
 *
 * Usage:
 * ```
 * val camera = CameraProvider.Builder().center(point).zoom(17.0).build()
 * MapAnimationHelper.easeTo(mapboxMap, camera, durationMs = 1000)
 * MapAnimationHelper.flyTo(mapboxMap, camera, durationMs = 2000)
 * ```
 */
object MapAnimationHelper {

    /**
     * Smoothly transition the camera using [easeTo].
     */
    fun easeTo(
        mapboxMap: MapboxMap,
        camera: CameraProvider,
        durationMs: Long = 1000,
        interpolator: Interpolator = LinearInterpolator(),
    ) {
        mapboxMap.easeTo(
            camera.toMapbox(),
            MapAnimationOptions.mapAnimationOptions {
                duration(durationMs)
                interpolator(interpolator)
            }
        )
    }

    /**
     * Fly the camera using [flyTo].
     */
    fun flyTo(
        mapboxMap: MapboxMap,
        camera: CameraProvider,
        durationMs: Long = 2000,
    ) {
        mapboxMap.flyTo(
            camera.toMapbox(),
            MapAnimationOptions.mapAnimationOptions {
                duration(durationMs)
            }
        )
    }
}
