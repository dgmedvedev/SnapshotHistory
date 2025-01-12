package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class RequestPermissionsUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.requestPermissions()
    }
}