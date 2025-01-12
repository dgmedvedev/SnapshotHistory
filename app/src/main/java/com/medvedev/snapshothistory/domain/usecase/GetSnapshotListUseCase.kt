package com.medvedev.snapshothistory.domain.usecase

import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class GetSnapshotListUseCase(private val repository: SnapshotRepository) {
    operator fun invoke(): LiveData<List<Snapshot>> {
        return repository.getSnapshotList()
    }
}