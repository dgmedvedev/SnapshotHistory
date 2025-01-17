package com.medvedev.snapshothistory.data.manager.camera

import android.content.ContentResolver
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface CameraManager {
    fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView)
    fun stopCamera()
    fun takeSnapshot(
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    )
}