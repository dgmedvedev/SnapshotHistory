package com.medvedev.snapshothistory.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "snapshots")
class SnapshotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Date = Date(),
    val latitude: Double,
    val longitude: Double,
    val name: String
)