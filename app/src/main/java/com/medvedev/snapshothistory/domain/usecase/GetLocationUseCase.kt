package com.medvedev.snapshothistory.domain.usecase

import android.location.Location
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class GetLocationUseCase(private val repository: SnapshotRepository) {
    operator fun invoke(): Location? {
        return repository.getLocation()
    }
}