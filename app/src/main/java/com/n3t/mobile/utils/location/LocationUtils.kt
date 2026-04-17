package com.n3t.mobile.utils.location

import android.location.Location
import kotlin.math.roundToInt


object LocationUtils {
    // Convert location speed from m/s to km/h
    fun calculateSpeed(location: Location): Int {
        if(location.speed == null) return 0

        val speed = location.speed!!

        if(speed < 0) return 0

        val speedInKmH = speed * 3.6

        return speedInKmH.roundToInt()
    }
}

