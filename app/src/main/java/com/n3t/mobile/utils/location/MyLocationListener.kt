package com.n3t.mobile.utils.location

import android.location.Location

/**
 * Simple functional callback for location updates.
 */
fun interface MyLocationListener {
    fun onLocationChanged(location: Location)
}
