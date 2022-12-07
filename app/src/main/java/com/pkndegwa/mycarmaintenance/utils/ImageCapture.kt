package com.pkndegwa.mycarmaintenance.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * the [ImageCapture] class is used to request various permissions
 * such as accessing external storage or the camera.
 */
class ImageCapture(
    val context: Context,
    private val permission: String,
    private val fragmentActivity: Activity,
    private val requestPermissionLauncher: ActivityResultLauncher<String>
) {

    fun askForPermission() {
        if (ContextCompat.checkSelfPermission(context, permission) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    fragmentActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showDialog(
                    message = "The app needs permission to access your gallery."
                )
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        when {
            ActivityCompat.shouldShowRequestPermissionRationale(
                fragmentActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showDialog(
                    message = "The app needs to access your gallery to get an image of your vehicle"
                )
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    /**
     * Shows dialog saying why the app needs permission.
     * Only shown if the user has denied the permission request before.
     */
    private fun showDialog(message: String) {
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            .setTitle("My Car Maintenance App")
            .setMessage(message)
            .setPositiveButton("Request again") { dialog, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        builder.create().show()
    }
}