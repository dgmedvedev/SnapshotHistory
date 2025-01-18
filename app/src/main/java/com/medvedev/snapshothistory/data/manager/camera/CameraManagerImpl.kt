package com.medvedev.snapshothistory.data.manager.camera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun takeSnapshot(
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        val outputOptions: ImageCapture.OutputFileOptions

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())
        val snapshotName = "$name.jpg"
        val folderPath = getFolderPathFromUri(uri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, snapshotName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, folderPath) // Путь к папке
            }

            outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()
        } else {
            var outputDirectory: File
            outputDirectory = File("${Environment.getExternalStorageDirectory()}").let {
                File(it, folderPath).apply { mkdir() }
            }
            if (!outputDirectory.exists()) outputDirectory =
                DEFAULT_PICTURES_DIRECTORY

            val file = File(outputDirectory, snapshotName)
            outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        }

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            imageSavedCallback
        )
    }

    private fun getFolderPathFromUri(uri: Uri?): String {
        if (uri == null) return EMPTY_PATH
        val path = uri.path ?: return EMPTY_PATH
        val split = path.split(SPLIT_DELIMITERS)
        var folderPath: String = EMPTY_PATH
        if (split.size > 1) {
            folderPath = split[1]
        }
        return folderPath
    }

    companion object {
        private const val EMPTY_PATH = ""
        private const val SPLIT_DELIMITERS = ":"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val DEFAULT_PICTURES_DIRECTORY =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }
}