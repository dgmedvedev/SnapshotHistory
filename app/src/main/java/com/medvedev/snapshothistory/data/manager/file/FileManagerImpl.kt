package com.medvedev.snapshothistory.data.manager.file

import android.net.Uri
import android.os.Environment
import java.io.File

class FileManagerImpl : FileManager {

    override fun getFolderPathFromUri(uri: Uri?): String {
        if (uri == null) return EMPTY_PATH
        val path = uri.path ?: return EMPTY_PATH
        val split = path.split(SPLIT_DELIMITERS)
        var folderPath: String = EMPTY_PATH
        if (split.size > 1) {
            folderPath = split[1]
        }
        return folderPath
    }

    override fun getOutputDirectory(folderPath: String): File {
        val outputDirectory = File("${Environment.getExternalStorageDirectory()}").let {
            File(it, folderPath).apply { mkdir() }
        }
        return if (outputDirectory.exists()) outputDirectory else DEFAULT_PICTURES_DIRECTORY
    }

    companion object {
        private const val EMPTY_PATH = ""
        private const val SPLIT_DELIMITERS = ":"
        private val DEFAULT_PICTURES_DIRECTORY =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }
}