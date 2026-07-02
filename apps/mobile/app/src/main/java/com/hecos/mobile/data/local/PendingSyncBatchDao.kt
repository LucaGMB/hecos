package com.hecos.mobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PendingSyncBatchDao {

    @Insert
    suspend fun insert(batch: PendingSyncBatch): Long

    @Query("SELECT * FROM pending_sync_batches ORDER BY createdAt ASC")
    suspend fun getAll(): List<PendingSyncBatch>

    @Query("DELETE FROM pending_sync_batches WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE pending_sync_batches SET attemptCount = attemptCount + 1 WHERE id = :id")
    suspend fun incrementAttemptCount(id: Long)
}
