package com.medvedev.snapshothistory.domain.usecase

import android.graphics.Bitmap
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class SaveSnapshotUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(snapshot: Bitmap, directory: String): Boolean {
        return repository.saveSnapshot(snapshot = snapshot, directory = directory)
    }
}