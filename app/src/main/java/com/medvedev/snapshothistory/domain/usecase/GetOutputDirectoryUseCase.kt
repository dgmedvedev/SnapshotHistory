package com.medvedev.snapshothistory.domain.usecase

import android.net.Uri
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository
import java.io.File

class GetOutputDirectoryUseCase(private val repository: SnapshotRepository) {
    operator fun invoke(uri: Uri?): File {
        return repository.getOutputDirectory(uri = uri)
    }
}