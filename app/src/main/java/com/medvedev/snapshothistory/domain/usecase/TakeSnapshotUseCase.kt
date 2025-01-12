package com.medvedev.snapshothistory.domain.usecase

import android.graphics.Bitmap
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class TakeSnapshotUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(): Bitmap {
        return repository.takeSnapshot()
    }
}