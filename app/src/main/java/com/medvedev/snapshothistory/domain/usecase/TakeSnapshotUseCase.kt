package com.medvedev.snapshothistory.domain.usecase

import android.content.ContentResolver
import androidx.camera.core.ImageCapture
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class TakeSnapshotUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        return repository.takeSnapshot(
            contentResolver = contentResolver,
            imageSavedCallback = imageSavedCallback
        )
    }
}