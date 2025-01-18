package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class GetSnapshotUseCase(private val repository: SnapshotRepository) {
    suspend operator fun invoke(id: Int): Snapshot {
        return repository.getSnapshot(snapshotId = id)
    }
}