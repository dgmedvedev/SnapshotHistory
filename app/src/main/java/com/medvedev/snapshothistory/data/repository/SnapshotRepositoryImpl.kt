package com.medvedev.snapshothistory.data.repository

import android.content.ContentResolver
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.medvedev.snapshothistory.data.database.dao.SnapshotDao
import com.medvedev.snapshothistory.data.database.mapper.toDomain
import com.medvedev.snapshothistory.data.database.mapper.toEntity
import com.medvedev.snapshothistory.data.database.mapper.toSnapshotDomainList
import com.medvedev.snapshothistory.data.manager.camera.CameraManager
import com.medvedev.snapshothistory.data.manager.file.FileManager
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository
import java.io.File

class SnapshotRepositoryImpl(
    private val cameraManager: CameraManager,
    private val fileManager: FileManager,
    private val snapshotDao: SnapshotDao
) : SnapshotRepository {
    override suspend fun getSnapshot(snapshotId: Int): Snapshot =
        snapshotDao.getSnapshot(snapshotId).toDomain()

    override fun getOutputDirectory(uri: Uri?): File =
        fileManager.getOutputDirectory(uri)

    override suspend fun addSnapshot(snapshot: Snapshot) {
        snapshotDao.addSnapshot(snapshot.toEntity())
    }

    override suspend fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView) {
        cameraManager.startCamera(lifecycleOwner, viewFinder)
    }

    override fun stopCamera() {
        cameraManager.stopCamera()
    }

    override fun takeSnapshot(
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        cameraManager.takeSnapshot(uri, contentResolver, imageSavedCallback)
    }

    override fun configureSaveDirectory(directory: String) {
        TODO("Not yet implemented")
    }

    override fun getSnapshotList(): LiveData<List<Snapshot>> =
        MediatorLiveData<List<Snapshot>>().apply {
            addSource(snapshotDao.getSnapshotList()) {
                value = it.toSnapshotDomainList()
            }
        }

    companion object {
        const val LOG_TAG = "CameraX_TEST"
    }
}