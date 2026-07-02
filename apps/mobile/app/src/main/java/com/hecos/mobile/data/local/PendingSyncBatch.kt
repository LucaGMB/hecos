package com.hecos.mobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A batch of Health Connect records for a given type that failed to reach the server.
 * Kept locally so it can be retried on the next manual or automatic sync.
 */
@Entity(tableName = "pending_sync_batches")
data class PendingSyncBatch(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val recordsJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val attemptCount: Int = 0
)
