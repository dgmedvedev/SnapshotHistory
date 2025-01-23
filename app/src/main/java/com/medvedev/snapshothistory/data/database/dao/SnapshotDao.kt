package com.medvedev.snapshothistory.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medvedev.snapshothistory.data.database.model.SnapshotEntity

@Dao
interface SnapshotDao {
    @Query("SELECT*FROM snapshots ORDER BY date")
    fun getSnapshotList(): LiveData<List<SnapshotEntity>>

    @Query("SELECT*FROM snapshots WHERE id=:snapshotId LIMIT 1")
    suspend fun getSnapshot(snapshotId: Int): SnapshotEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSnapshot(snapshotEntity: SnapshotEntity)

    @Delete
    suspend fun deleteSnapshot(snapshotEntity: SnapshotEntity)
}