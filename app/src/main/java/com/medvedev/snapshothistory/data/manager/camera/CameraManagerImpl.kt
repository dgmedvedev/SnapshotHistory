package com.medvedev.snapshothistory.data.manager.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl

class CameraManagerImpl(private val context: Context) : CameraManager {

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null

    override fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture?.addListener({
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            cameraProvider = cameraProviderFuture?.get()
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e(
                    SnapshotRepositoryImpl.LOG_TAG,
                    "CameraRepositoryImpl: Use case binding failed",
                    exc
                )
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    override fun takeSnapshot() {
        TODO("Not yet implemented")
    }
}