package com.medvedev.snapshothistory.app.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.medvedev.snapshothistory.data.manager.camera.CameraManagerImpl
import com.medvedev.snapshothistory.data.manager.file.FileManagerImpl
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl
import com.medvedev.snapshothistory.domain.usecase.GetOutputDirectoryUseCase
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.StopCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.TakeSnapshotUseCase

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val cameraManager by lazy {
        CameraManagerImpl(context = context)
    }

    private val fileManager by lazy {
        FileManagerImpl(context = context)
    }

    private val snapshotRepository by lazy {
        SnapshotRepositoryImpl(
            cameraManager = cameraManager,
            fileManager = fileManager
        )
    }

    private val getOutputDirectoryUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetOutputDirectoryUseCase(repository = snapshotRepository)
    }

    private val startCameraUseCase by lazy(LazyThreadSafetyMode.NONE) {
        StartCameraUseCase(repository = snapshotRepository)
    }

    private val stopCameraUseCase by lazy(LazyThreadSafetyMode.NONE) {
        StopCameraUseCase(repository = snapshotRepository)
    }

    private val takeSnapshotUseCase by lazy(LazyThreadSafetyMode.NONE) {
        TakeSnapshotUseCase(repository = snapshotRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getOutputDirectoryUseCase = getOutputDirectoryUseCase,
            startCameraUseCase = startCameraUseCase,
            stopCameraUseCase = stopCameraUseCase,
            takeSnapshotUseCase = takeSnapshotUseCase
        ) as T
    }
}