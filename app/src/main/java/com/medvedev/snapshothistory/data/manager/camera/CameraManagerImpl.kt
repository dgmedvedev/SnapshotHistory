package com.medvedev.snapshothistory.data.manager.camera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.File

class CameraManagerImpl(private val context: Context) : CameraManager {

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null

    override fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture?.addListener({
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = viewFinder.surfaceProvider
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
            } catch (_: Exception) {
            }
        }, ContextCompat.getMainExecutor(context))
    }

    override fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    override fun takeSnapshot(
        snapshotName: String,
        folderPath: String,
        outputDirectory: File,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        val outputOptions: ImageCapture.OutputFileOptions

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, snapshotName)
                put(MediaStore.Images.Media.MIME_TYPE, JPEG_TYPE)
                put(MediaStore.Images.Media.RELATIVE_PATH, folderPath)
            }
            outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()
        } else {
            val file = File(outputDirectory, snapshotName)
            outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        }
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            imageSavedCallback
        )
    }

    companion object {
        private const val JPEG_TYPE = "image/jpeg"
    }
}