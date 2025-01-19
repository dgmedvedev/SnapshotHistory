package com.medvedev.snapshothistory.domain.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.domain.model.Snapshot
import java.io.File

interface SnapshotRepository {
    suspend fun getSnapshot(snapshotId: Int): Snapshot
    suspend fun saveSnapshot(snapshot: Bitmap, directory: String): Boolean
    suspend fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView)
    fun takeSnapshot(
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    )

    fun stopCamera()
    fun getOutputDirectory(uri: Uri?): File
    fun configureSaveDirectory(directory: String)
    fun getSnapshotList(): LiveData<List<Snapshot>>
}