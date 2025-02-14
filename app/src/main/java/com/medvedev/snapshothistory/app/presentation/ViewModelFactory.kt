package com.medvedev.snapshothistory.app.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.data.database.SnapshotDatabase
import com.medvedev.snapshothistory.data.manager.camera.CameraManagerImpl
import com.medvedev.snapshothistory.data.manager.file.FileManagerImpl
import com.medvedev.snapshothistory.data.manager.location.AppLocationManagerImpl
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl
import com.medvedev.snapshothistory.domain.usecase.AddSnapshotUseCase
import com.medvedev.snapshothistory.domain.usecase.GetLocationUseCase
import com.medvedev.snapshothistory.domain.usecase.GetSnapshotListUseCase
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.TakeSnapshotUseCase

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val appLocationManager by lazy { AppLocationManagerImpl(context = context) }

    private val cameraManager by lazy { CameraManagerImpl(context = context) }

    private val fileManager by lazy { FileManagerImpl() }

    private val snapshotDao by lazy {
        SnapshotDatabase.getInstance(context = context).snapshotDao()
    }

    private val snapshotRepository by lazy {
        SnapshotRepositoryImpl(
            appLocationManager = appLocationManager,
            cameraManager = cameraManager,
            fileManager = fileManager,
            snapshotDao = snapshotDao
        )
    }

    private val addSnapshotUseCase by lazy(LazyThreadSafetyMode.NONE) {
        AddSnapshotUseCase(repository = snapshotRepository)
    }

    private val getLocationUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetLocationUseCase(repository = snapshotRepository)
    }

    private val getSnapshotListUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetSnapshotListUseCase(repository = snapshotRepository)
    }

    private val startCameraUseCase by lazy(LazyThreadSafetyMode.NONE) {
        StartCameraUseCase(repository = snapshotRepository)
    }

    private val takeSnapshotUseCase by lazy(LazyThreadSafetyMode.NONE) {
        TakeSnapshotUseCase(repository = snapshotRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                addSnapshotUseCase = addSnapshotUseCase,
                getLocationUseCase = getLocationUseCase,
                startCameraUseCase = startCameraUseCase,
                takeSnapshotUseCase = takeSnapshotUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(SnapshotListViewModel::class.java)) {
            return SnapshotListViewModel(
                getSnapshotListUseCase = getSnapshotListUseCase
            ) as T
        }
        throw IllegalArgumentException(context.getString(R.string.unknown_viewmodel_class))
    }
}