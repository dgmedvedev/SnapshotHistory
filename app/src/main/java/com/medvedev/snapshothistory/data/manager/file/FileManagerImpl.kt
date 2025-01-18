package com.medvedev.snapshothistory.data.manager.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File

class FileManagerImpl(private val context: Context) : FileManager {

    override fun getOutputDirectory(uri: Uri?): File {
        val folderPath = getFolderPathFromUri(uri)
        return getDirectoryFromPath(folderPath)
    }

    private fun getFolderPathFromUri(uri: Uri?): String {
        if (uri == null) return EMPTY_PATH
        val path = uri.path ?: return EMPTY_PATH
        val split = path.split(":")
        var folderPath: String = EMPTY_PATH
        if (split.size > 1) {
            folderPath = split[1]
        }
        return folderPath
    }

    private fun getDirectoryFromPath(folderPath: String): File {
        val outputDirectory = File("${Environment.getExternalStorageDirectory()}").let {
            File(it, folderPath).apply { mkdir() }
        }
        return if (outputDirectory.exists()) outputDirectory else context.filesDir
    }

    companion object {
        private const val EMPTY_PATH = ""
    }
}