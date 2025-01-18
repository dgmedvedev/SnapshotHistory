package com.medvedev.snapshothistory.domain.usecase

import android.content.ContentResolver
import android.net.Uri
import androidx.camera.core.ImageCapture
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class TakeSnapshotUseCase(private val repository: SnapshotRepository) {
    operator fun invoke(
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        return repository.takeSnapshot(
            uri = uri,
            contentResolver = contentResolver,
            imageSavedCallback = imageSavedCallback
        )
    }
}