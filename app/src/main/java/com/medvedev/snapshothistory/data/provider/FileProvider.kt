package com.medvedev.snapshothistory.data.provider

interface FileProvider {
    fun saveSnapshot(): Boolean
}