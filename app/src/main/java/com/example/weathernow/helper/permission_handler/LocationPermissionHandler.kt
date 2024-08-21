package com.example.weathernow.helper.permission_handler

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weathernow.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LocationPermissionHandler(private val activity: AppCompatActivity,private val onPermissionGranted: () -> Unit,) {

    private val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )


    private val permissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    fun checkAndRequestPermissions() {
        when {
            areAllPermissionsGranted() -> onPermissionGranted()
            shouldShowRequestPermissionRationale() -> showPermissionRationale()
            else -> requestPermissions()
        }
    }

    private fun areAllPermissionsGranted() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale() = requiredPermissions.any {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
    }

    private fun showPermissionRationale() {
        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.location_required))
            .setMessage(activity.getString(R.string.permission_dialog))
            .setPositiveButton(activity.getString(R.string.grant)) { _, _ -> requestPermissions() }
            .setNegativeButton(activity.getString(R.string.deny)) { _, _ -> onPermissionDenied() }
            .show()
    }

    private fun requestPermissions() {
        permissionLauncher.launch(requiredPermissions)
    }

    private fun onPermissionDenied() {
        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.permission_denied))
            .setMessage(activity.getString(R.string.open_settings))
            .setPositiveButton(activity.getString(R.string.open_setting)) { _, _ -> openAppSettings() }
            .setNegativeButton(activity.getString(R.string.cancel)) { _, _ -> /* Handle the case when user doesn't want to grant permission */ }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }
}