package com.medvedev.snapshothistory.data.manager.file

interface FileManager {
    fun saveSnapshot(): Boolean
}