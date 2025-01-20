package com.medvedev.snapshothistory.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medvedev.snapshothistory.data.database.converter.SnapshotTypeConverters
import com.medvedev.snapshothistory.data.database.dao.SnapshotDao
import com.medvedev.snapshothistory.data.database.model.SnapshotEntity

@Database(entities = [SnapshotEntity::class], version = 1, exportSchema = false)
@TypeConverters(SnapshotTypeConverters::class)
abstract class SnapshotDatabase : RoomDatabase() {

    abstract fun snapshotDao(): SnapshotDao

    companion object {
        private const val DB_NAME = "snapshots.db"
        private var INSTANCE: SnapshotDatabase? = null

        fun getInstance(context: Context): SnapshotDatabase {
            INSTANCE?.let { return it }
            synchronized(SnapshotDatabase::class.java) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    SnapshotDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}