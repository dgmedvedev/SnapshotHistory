package com.medvedev.snapshothistory.data.manager.file

import android.net.Uri
import java.io.File

interface FileManager {
    fun getOutputDirectory(uri: Uri?): File
}