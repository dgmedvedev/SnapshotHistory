package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class StartCameraUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke() {
        return repository.startCamera()
    }
}