package com.medvedev.snapshothistory.domain.permissions

interface PermissionChecker {
    fun allPermissionsGranted(): Boolean
    fun requestPermissions()
    fun startCamera()
}