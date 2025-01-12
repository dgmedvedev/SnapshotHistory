package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class StopCameraUseCase(private val repository: SnapshotRepository) {
    operator fun invoke() {
        repository.stopCamera()
    }
}