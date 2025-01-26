package com.medvedev.snapshothistory.domain.repository

import android.content.ContentResolver
import android.location.Location
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.medvedev.snapshothistory.domain.model.Snapshot

interface SnapshotRepository {
    suspend fun addSnapshot(snapshot: Snapshot)
    suspend fun getSnapshot(snapshotId: Int): Snapshot
    suspend fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView)
    fun takeSnapshot(
        snapshotName: String,
        uri: Uri?,
        contentResolver: ContentResolver,
        imageSavedCallback: ImageCapture.OnImageSavedCallback
    )

    fun getLocation(): Location?
    fun getSnapshotList(): LiveData<List<Snapshot>>
}