package com.medvedev.snapshothistory.data.manager.camera

import android.content.ContentResolver
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import java.io.File

interface CameraManager {
    fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView)
    fun stopCamera()
    fun takeSnapshot(
        folderPath: String,
        outputDirectory: File,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    )
}