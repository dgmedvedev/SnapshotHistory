package com.medvedev.snapshothistory.app.presentation

import android.Manifest
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl
import com.medvedev.snapshothistory.domain.usecase.AddSnapshotUseCase
import com.medvedev.snapshothistory.domain.usecase.GetOutputDirectoryUseCase
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.StopCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.TakeSnapshotUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val addSnapshotUseCase: AddSnapshotUseCase,
    private val getOutputDirectoryUseCase: GetOutputDirectoryUseCase,
    private val startCameraUseCase: StartCameraUseCase,
    private val stopCameraUseCase: StopCameraUseCase,
    private val takeSnapshotUseCase: TakeSnapshotUseCase
) : ViewModel() {

    private var _resultPhotoCapture = MutableLiveData<String>()
    val resultPhotoCapture: LiveData<String>
        get() = _resultPhotoCapture

    fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView) {
        viewModelScope.launch { startCameraUseCase(lifecycleOwner, viewFinder) }
    }

    fun stopCamera() {
        stopCameraUseCase()
    }

    fun onCameraButtonPressed(uri: Uri?, contentResolver: ContentResolver) {
        viewModelScope.launch {
            takeSnapshotUseCase(uri, contentResolver,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(
                            SnapshotRepositoryImpl.LOG_TAG,
                            "Photo capture failed: ${exc.message}",
                            exc
                        )
                        _resultPhotoCapture.value = exc.message
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        Log.d(SnapshotRepositoryImpl.LOG_TAG, msg)
                        _resultPhotoCapture.value = output.savedUri.toString()
                    }
                }
            )
        }
    }

    companion object {
        val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}