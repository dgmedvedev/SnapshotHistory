package com.medvedev.snapshothistory.data.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.data.provider.CameraProvider
import com.medvedev.snapshothistory.data.provider.FileProvider
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class SnapshotRepositoryImpl(
    private val cameraProvider: CameraProvider,
    //private val fileProvider: FileProvider
) : SnapshotRepository {
    override suspend fun getSnapshot(snapshotId: Int): Snapshot {
        TODO("Not yet implemented")
    }

    override suspend fun requestPermissions(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveSnapshot(snapshot: Bitmap, directory: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun startCamera() {
        cameraProvider.startCamera()
    }

    override fun stopCamera() {
        cameraProvider.stopCamera()
    }

    override suspend fun takeSnapshot(): Bitmap {
        TODO("Not yet implemented")
    }

    override fun configureSaveDirectory(directory: String) {
        TODO("Not yet implemented")
    }

    override fun getSnapshotList(): LiveData<List<Snapshot>> {
        TODO("Not yet implemented")
    }
}