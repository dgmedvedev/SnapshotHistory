package com.medvedev.snapshothistory.domain.usecase

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class StartCameraUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        repository.startCamera(lifecycleOwner = lifecycleOwner, previewView = previewView)
    }
}