package com.n3t.mobile.utils.location

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.n3t.mobile.R


class LocationPermissionHelper(private val context: Context) {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
    interface PermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    fun checkLocationPermission(listener: PermissionListener) {
        if (isLocationPermissionGranted()) {
            listener.onPermissionGranted()
        } else {
            requestLocationPermission(listener)
        }
    }

     fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission(listener: PermissionListener) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // User has denied location permission before, show rationale.
            listener.onPermissionDenied()
            showPermissionDeniedDialog()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        listener: PermissionListener,
        showDialogWhenDenied: Boolean = true
    ) {

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                listener.onPermissionGranted()
            } else {
                // Permission denied.
                listener.onPermissionDenied()

                if (showDialogWhenDenied) {
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        val dialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.location_permission_title))
            .setMessage(context.getString(R.string.location_permission_message))
            .setPositiveButton(context.getString(R.string.location_permission_go_to_setting)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                // go to location setting
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setNegativeButton(context.getString(R.string.location_permission_cancel)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }
}

