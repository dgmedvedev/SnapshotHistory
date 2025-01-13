package com.medvedev.snapshothistory.data.permission

interface PermissionController {
    fun checkPermissions(permissions: Array<String>): Boolean
}