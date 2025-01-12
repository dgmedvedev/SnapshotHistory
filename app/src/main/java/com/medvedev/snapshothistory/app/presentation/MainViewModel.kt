package com.medvedev.snapshothistory.app.presentation

import android.Manifest
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    companion object {
        const val TAG = "CameraX_TEST"
        const val REQUEST_CODE_PERMISSIONS = 100
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}