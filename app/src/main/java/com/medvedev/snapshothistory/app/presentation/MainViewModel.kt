package com.medvedev.snapshothistory.app.presentation

import android.Manifest
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.usecase.AddSnapshotUseCase
import com.medvedev.snapshothistory.domain.usecase.GetLocationUseCase
import com.medvedev.snapshothistory.domain.usecase.StartCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.StopCameraUseCase
import com.medvedev.snapshothistory.domain.usecase.TakeSnapshotUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val addSnapshotUseCase: AddSnapshotUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val startCameraUseCase: StartCameraUseCase,
    private val stopCameraUseCase: StopCameraUseCase,
    private val takeSnapshotUseCase: TakeSnapshotUseCase
) : ViewModel() {

    private var _resultPhotoCapture = MutableLiveData<String>()
    val resultPhotoCapture: LiveData<String>
        get() = _resultPhotoCapture

    fun startCamera(lifecycleOwner: LifecycleOwner, viewFinder: PreviewView) {
        viewModelScope.launch { startCameraUseCase(lifecycleOwner, viewFinder) }
    }

    fun stopCamera() {
        stopCameraUseCase()
    }

    fun onCameraButtonPressed(
        uri: Uri?,
        contentResolver: ContentResolver
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        val name = SimpleDateFormat(NAME_FORMAT, Locale.getDefault())
            .format(currentTimeMillis)
        val snapshotName = String.format(DISPLAY_NAME_FORMAT, name)

        viewModelScope.launch {
            takeSnapshotUseCase(snapshotName, uri, contentResolver,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(
                            SnapshotRepositoryImpl.LOG_TAG,
                            "Photo capture failed: ${exc.message}",
                            exc
                        )
                        _resultPhotoCapture.value = exc.message
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val date = Date(currentTimeMillis)
                        val location = getLocationUseCase()

                        Log.d(SnapshotRepositoryImpl.LOG_TAG, "location: $location")

                        val latitude = location?.latitude ?: DEFAULT_LOCATION
                        val longitude = location?.longitude ?: DEFAULT_LOCATION
                        val filePath = output.savedUri?.toString() ?: UNDEFINED_FILE_PATH
                        val snapshot = Snapshot(date, latitude, longitude, snapshotName, filePath)
                        Log.d(SnapshotRepositoryImpl.LOG_TAG, "$snapshot")
                        addSnapshotToDB(snapshot = snapshot)
                        _resultPhotoCapture.value = output.savedUri.toString()
                    }
                }
            )
        }
    }

    private fun addSnapshotToDB(snapshot: Snapshot) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { addSnapshotUseCase(snapshot = snapshot) }
        }
    }

    companion object {
        private const val NAME_FORMAT = "ddMMyyyy_HH-mm-ss"
        private const val DISPLAY_NAME_FORMAT = "%s.jpg"
        private const val UNDEFINED_FILE_PATH = ""
        private const val DEFAULT_LOCATION = 0.0
        val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}