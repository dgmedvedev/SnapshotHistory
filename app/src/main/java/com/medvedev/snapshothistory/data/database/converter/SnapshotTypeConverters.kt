package com.medvedev.snapshothistory.data.database.converter

import androidx.room.TypeConverter
import java.util.Date

class SnapshotTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let { Date(it) }
    }
}