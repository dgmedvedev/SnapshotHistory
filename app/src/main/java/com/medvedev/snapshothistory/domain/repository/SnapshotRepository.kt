package com.medvedev.snapshothistory.domain.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.domain.model.Snapshot

interface SnapshotRepository {
    suspend fun getSnapshot(snapshotId: Int): Snapshot
    suspend fun checkPermissions(permissions: Array<String>): Boolean
    suspend fun saveSnapshot(snapshot: Bitmap, directory: String): Boolean
    suspend fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView)
    suspend fun takeSnapshot(
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    )

    fun stopCamera()
    fun configureSaveDirectory(directory: String)
    fun getSnapshotList(): LiveData<List<Snapshot>>
}