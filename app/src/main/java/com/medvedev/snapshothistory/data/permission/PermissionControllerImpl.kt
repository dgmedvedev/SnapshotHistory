package com.medvedev.snapshothistory.data.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionControllerImpl(private val context: Context) : PermissionController {
    override fun checkPermissions(permissions: Array<String>): Boolean =
        permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
}