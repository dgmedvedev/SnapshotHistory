package com.medvedev.snapshothistory.domain.model

import java.util.Date

data class Snapshot(
    val id: Int = UNDEFINED_ID,
    val date: Date = Date(),
    val latitude: Double,
    val longitude: Double,
    val name: String
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}