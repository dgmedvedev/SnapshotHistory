package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class CheckPermissionsUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(permissions: Array<String>): Boolean {
        return repository.checkPermissions(permissions)
    }
}