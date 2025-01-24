package com.medvedev.snapshothistory.data.repository

import android.content.ContentResolver
import android.location.Location
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
import com.medvedev.snapshothistory.data.manager.location.AppLocationManager
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class SnapshotRepositoryImpl(
    private val appLocationManager: AppLocationManager,
    private val cameraManager: CameraManager,
    private val fileManager: FileManager,
    private val snapshotDao: SnapshotDao
) : SnapshotRepository {

    override suspend fun addSnapshot(snapshot: Snapshot) {
        snapshotDao.addSnapshot(snapshot.toEntity())
    }

    override fun configureSaveDirectory(directory: String) {
        TODO("Not yet implemented")
    }

    override fun getLocation(): Location? =
        appLocationManager.getLocation()

    override fun getSnapshotList(): LiveData<List<Snapshot>> =
        MediatorLiveData<List<Snapshot>>().apply {
            addSource(snapshotDao.getSnapshotList()) {
                value = it.toSnapshotDomainList()
            }
        }

    override suspend fun getSnapshot(snapshotId: Int): Snapshot =
        snapshotDao.getSnapshot(snapshotId).toDomain()

    override suspend fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView) {
        cameraManager.startCamera(lifecycleOwner, viewFinder)
    }

    override fun stopCamera() {
        cameraManager.stopCamera()
    }

    override fun takeSnapshot(
        snapshotName: String,
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        val folderPath = fileManager.getFolderPathFromUri(uri)
        val outputDirectory = fileManager.getOutputDirectory(folderPath)
        cameraManager.takeSnapshot(
            snapshotName, folderPath, outputDirectory, contentResolver, imageSavedCallback
        )
    }

    companion object {
        const val LOG_TAG = "CameraX_TEST"
    }
}