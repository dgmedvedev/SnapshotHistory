package com.medvedev.snapshothistory.data.manager.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface CameraManager {
    fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView)
    fun stopCamera()
    fun takeSnapshot()
}