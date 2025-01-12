package com.medvedev.snapshothistory.app.presentation

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.StopCameraUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val startCameraUseCase: StartCameraUseCase,
    private val stopCameraUseCase: StopCameraUseCase
) : ViewModel() {

    fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        viewModelScope.launch {
            startCameraUseCase(lifecycleOwner, previewView)
        }
    }

    fun stopCamera() {
        stopCameraUseCase()
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 100
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}