package com.medvedev.snapshothistory.data.repository

import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.data.manager.camera.CameraManager
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class SnapshotRepositoryImpl(
    private val cameraManager: CameraManager,
//    private val fileManager: FileManager
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

    override suspend fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        cameraManager.startCamera(lifecycleOwner, previewView)
    }

    override fun stopCamera() {
        cameraManager.stopCamera()
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

    companion object {
        const val LOG_TAG = "CameraX_TEST"
    }
}