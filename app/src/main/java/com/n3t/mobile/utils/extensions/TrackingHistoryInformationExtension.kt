package com.n3t.mobile.utils.extensions

import android.location.Location
import com.n3t.mobile.data.model.tracking.TrackingHistoryInformation

fun TrackingHistoryInformation.distance(destination: TrackingHistoryInformation) : Double {
    val results = floatArrayOf(1f)

    Location.distanceBetween(
        this.lat,
        this.lon,
        destination.lat,
        destination.lon,
        results
    )

    return results[0].toDouble()
}

