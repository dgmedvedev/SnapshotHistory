package com.medvedev.snapshothistory.data.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.data.manager.camera.CameraManager
import com.medvedev.snapshothistory.data.permission.PermissionController
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.repository.SnapshotRepository

class SnapshotRepositoryImpl(
    private val permissionController: PermissionController,
    private val cameraManager: CameraManager,
//    private val fileManager: FileManager
) : SnapshotRepository {
    override suspend fun getSnapshot(snapshotId: Int): Snapshot {
        TODO("Not yet implemented")
    }

    override suspend fun checkPermissions(permissions: Array<String>): Boolean =
        permissionController.checkPermissions(permissions)

    override suspend fun saveSnapshot(snapshot: Bitmap, directory: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView) {
        cameraManager.startCamera(lifecycleOwner, viewFinder)
    }

    override fun stopCamera() {
        cameraManager.stopCamera()
    }

    override suspend fun takeSnapshot(
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    ) {
        cameraManager.takeSnapshot(
            contentResolver = contentResolver,
            imageSavedCallback = imageSavedCallback
        )
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