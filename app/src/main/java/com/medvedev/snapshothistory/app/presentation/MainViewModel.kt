package com.medvedev.snapshothistory.app.presentation

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.snapshothistory.domain.usecase.CheckPermissionsUseCase
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.StopCameraUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val checkPermissionsUseCase: CheckPermissionsUseCase,
    private val startCameraUseCase: StartCameraUseCase,
    private val stopCameraUseCase: StopCameraUseCase
) : ViewModel() {

    private var _hasPermissions = MutableLiveData<Boolean>()
    val hasPermissions: LiveData<Boolean>
        get() = _hasPermissions

    init {
        viewModelScope.launch {
            _hasPermissions.value = checkPermissionsUseCase(REQUIRED_PERMISSIONS)
        }
    }

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