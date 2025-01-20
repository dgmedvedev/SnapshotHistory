package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class AddSnapshotUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(snapshot: Snapshot) {
        return repository.addSnapshot(snapshot = snapshot)
    }
}