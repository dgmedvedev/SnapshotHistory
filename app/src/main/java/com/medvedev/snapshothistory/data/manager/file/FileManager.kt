package com.medvedev.snapshothistory.data.manager.file

import android.net.Uri
import java.io.File

interface FileManager {
    fun getFolderPathFromUri(uri: Uri?): String
    fun getOutputDirectory(folderPath: String): File
}