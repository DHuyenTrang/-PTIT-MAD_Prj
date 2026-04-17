package com.n3t.mobile.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/**
 * Location manager that wraps FusedLocationProviderClient for clean usage.
 * Uses HIGH_ACCURACY priority with 1-second interval.
 */
class MyLocationManager(private val context: Context) {

    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback? = null

    /**
     * Start continuous location updates.
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(listener: MyLocationListener) {
        stopLocationUpdates()

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // 1 second interval
        )
            .setMinUpdateIntervalMillis(500L)
            .setMinUpdateDistanceMeters(1f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    listener.onLocationChanged(location)
                }
            }
        }

        fusedClient.requestLocationUpdates(
            request,
            androidx.core.content.ContextCompat.getMainExecutor(context),
            locationCallback!!
        )
    }

    /**
     * Stop location updates and clean up callback.
     */
    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedClient.removeLocationUpdates(it)
        }
        locationCallback = null
    }

    /**
     * Get last known location as a one-shot call.
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(callback: (Location?) -> Unit) {
        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                callback(location)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
