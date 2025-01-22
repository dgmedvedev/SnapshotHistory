package com.medvedev.snapshothistory.domain.model

import java.util.Date

data class Snapshot(
    val date: Date = Date(),
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val filePath: String,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}