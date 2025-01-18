package com.medvedev.snapshothistory.domain.usecase

import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class ConfigureSaveDirectoryUseCase(private val repository: SnapshotRepository) {
    operator fun invoke(directory: String) {
        repository.configureSaveDirectory(directory = directory)
    }
}