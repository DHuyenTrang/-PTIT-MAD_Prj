package com.n3t.mobile.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.n3t.mobile.R
import com.n3t.mobile.ui.map.MapFragment
import com.n3t.mobile.ui.permissions.LocationPermissionFragment

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (savedInstanceState == null) {
            checkPermissionsAndLoadFragment()
        }
    }

    private fun checkPermissionsAndLoadFragment() {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
            coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            loadMapFragment()
        } else {
            loadPermissionFragment()
        }
    }

    private fun loadPermissionFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.dashboard_fragment_container, LocationPermissionFragment())
            .commit()
    }

    private fun loadMapFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.dashboard_fragment_container, MapFragment())
            .commit()
    }

    fun onLocationPermissionGranted() {
        loadMapFragment()
    }
}
