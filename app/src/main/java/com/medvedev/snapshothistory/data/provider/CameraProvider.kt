package com.medvedev.snapshothistory.data.provider

interface CameraProvider {
    fun startCamera()
    fun stopCamera()
    fun takeSnapshot()
}