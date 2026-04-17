package com.n3t.mobile.utils.location

import java.util.LinkedList
import kotlin.math.abs

class MovingAverageFilter(private val windowSize: Int) {
    private val speedWindow = LinkedList<Float>()
    private val accelerationThreshold = 0.1f // acceleration threshold in m/s^2

    fun filter(speed: Float, acceleration: Float): Float {
        if (abs(acceleration) < accelerationThreshold) {
            return speed
        }

        speedWindow.add(speed)
        if (speedWindow.size > windowSize) {
            speedWindow.removeFirst()
        }
        return speedWindow.average().toFloat()
    }
}

