package com.medvedev.snapshothistory.app.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.medvedev.snapshothistory.data.manager.camera.CameraManagerImpl
import com.medvedev.snapshothistory.data.permission.PermissionControllerImpl
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl
import com.medvedev.snapshothistory.domain.usecase.CheckPermissionsUseCase
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.StopCameraUseCase

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val permissionController by lazy(LazyThreadSafetyMode.NONE) {
        PermissionControllerImpl(context = context)
    }

    private val cameraManager by lazy(LazyThreadSafetyMode.NONE) {
        CameraManagerImpl(context = context)
    }

    private val snapshotRepository by lazy(LazyThreadSafetyMode.NONE) {
        SnapshotRepositoryImpl(
            permissionController = permissionController,
            cameraManager = cameraManager
        )
    }

    private val checkPermissionsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        CheckPermissionsUseCase(repository = snapshotRepository)
    }

    private val startCameraUseCase by lazy(LazyThreadSafetyMode.NONE) {
        StartCameraUseCase(repository = snapshotRepository)
    }

    private val stopCameraUseCase by lazy(LazyThreadSafetyMode.NONE) {
        StopCameraUseCase(repository = snapshotRepository)
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            checkPermissionsUseCase = checkPermissionsUseCase,
            startCameraUseCase = startCameraUseCase,
            stopCameraUseCase = stopCameraUseCase
        ) as T
    }
}