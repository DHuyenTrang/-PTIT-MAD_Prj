package com.n3t.mobile.utils.extensions

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import com.mapbox.geojson.Point

fun Point.isApproxEqual(position: List<Double>?, epsilon: Double = 1e-6): Boolean {
    if (position == null || position.count() < 2) {
        return false
    }
    return Math.abs(this.latitude() - position[0]) < epsilon && Math.abs(this.longitude() - position[1]) < epsilon
}

fun Point.isPosition(position: List<Double>?): Boolean {
    if (position == null || position.count() < 2) {
        return false
    }
    return this.latitude() == position[0] && this.longitude() == position[1]
}


