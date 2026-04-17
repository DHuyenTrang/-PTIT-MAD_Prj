package com.n3t.mobile.utils

import com.mapbox.geojson.Point
import kotlin.math.abs

object RouteFormatUtils {

    fun formatDistance(distanceMeters: Int): String {
        return if (distanceMeters >= 1000) {
            val kilometers = distanceMeters / 1000.0
            if (abs(kilometers - kilometers.toInt()) < 0.05) {
                "${kilometers.toInt()} km"
            } else {
                String.format("%.1f km", kilometers)
            }
        } else {
            "$distanceMeters m"
        }
    }

    fun formatDuration(durationSeconds: Int): String {
        val hours = durationSeconds / 3600
        val minutes = (durationSeconds % 3600) / 60
        return when {
            hours > 0 && minutes > 0 -> "$hours giờ $minutes phút"
            hours > 0 -> "$hours giờ"
            minutes > 0 -> "$minutes phút"
            else -> "1 phút"
        }
    }

    fun parseGoogleDurationSeconds(duration: String?): Int {
        if (duration.isNullOrBlank()) return 0
        return duration.removeSuffix("s").toDoubleOrNull()?.toInt() ?: 0
    }

    fun decodePolyline(encoded: String): List<Point> {
        val coordinates = mutableListOf<Point>()
        var index = 0
        var latitude = 0
        var longitude = 0

        while (index < encoded.length) {
            var shift = 0
            var result = 0
            var byte: Int
            do {
                byte = encoded[index++].code - 63
                result = result or ((byte and 0x1f) shl shift)
                shift += 5
            } while (byte >= 0x20)
            latitude += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            shift = 0
            result = 0
            do {
                byte = encoded[index++].code - 63
                result = result or ((byte and 0x1f) shl shift)
                shift += 5
            } while (byte >= 0x20)
            longitude += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            coordinates += Point.fromLngLat(longitude / 1E5, latitude / 1E5)
        }

        return coordinates
    }
}
