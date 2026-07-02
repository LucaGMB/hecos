package com.hecos.mobile.data.repository

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.hecos.mobile.data.api.ApiClient
import com.hecos.mobile.data.health.HealthConnectReader
import com.hecos.mobile.data.local.PendingSyncBatch
import com.hecos.mobile.data.local.PendingSyncBatchDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SyncProgress(
    val currentType: String = "",
    val typesCompleted: Int = 0,
    val typesTotal: Int = 0,
    val totalSaved: Int = 0,
    val totalDuplicates: Int = 0,
    val errors: List<String> = emptyList(),
    val isComplete: Boolean = false
)

class SyncService(
    private val reader: HealthConnectReader,
    private val tokenStore: TokenStore,
    private val pendingSyncBatchDao: PendingSyncBatchDao
) {

    private val _progress = MutableStateFlow(SyncProgress())
    val progress: StateFlow<SyncProgress> = _progress

    suspend fun syncAll() {
        val token = tokenStore.getToken() ?: throw IllegalStateException("No token")
        val bearerToken = "Bearer $token"

        val pendingErrors = flushPendingQueue(bearerToken)

        val allData = reader.readAll()
        val total = allData.size
        var completed = 0
        var totalSaved = 0
        var totalDuplicates = 0
        val errors = mutableListOf<String>().apply { addAll(pendingErrors) }

        _progress.value = SyncProgress(typesTotal = total)

        for ((slug, records) in allData) {
            _progress.value = _progress.value.copy(
                currentType = slug,
                typesCompleted = completed
            )

            try {
                val response = ApiClient.api.syncRecords(slug, bearerToken, records)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    totalSaved += body.saved
                    totalDuplicates += body.duplicates
                } else {
                    errors.add("$slug: ${response.code()}")
                    persistForRetry(slug, records)
                }
            } catch (e: Exception) {
                errors.add("$slug: ${e.message}")
                persistForRetry(slug, records)
            }

            completed++
        }

        _progress.value = SyncProgress(
            typesCompleted = completed,
            typesTotal = total,
            totalSaved = totalSaved,
            totalDuplicates = totalDuplicates,
            errors = errors,
            isComplete = true
        )
    }

    /**
     * Attempts to resend previously failed batches stored in Room.
     * Successful batches are removed from the queue; failed ones are kept with
     * their attempt count incremented for a later retry.
     */
    private suspend fun flushPendingQueue(bearerToken: String): List<String> {
        val errors = mutableListOf<String>()
        val pending = pendingSyncBatchDao.getAll()

        for (batch in pending) {
            try {
                val records = JsonParser.parseString(batch.recordsJson).asJsonArray
                val response = ApiClient.api.syncRecords(batch.type, bearerToken, records)
                if (response.isSuccessful) {
                    pendingSyncBatchDao.deleteById(batch.id)
                } else {
                    pendingSyncBatchDao.incrementAttemptCount(batch.id)
                    errors.add("${batch.type} (pendiente): ${response.code()}")
                }
            } catch (e: Exception) {
                pendingSyncBatchDao.incrementAttemptCount(batch.id)
                errors.add("${batch.type} (pendiente): ${e.message}")
            }
        }

        return errors
    }

    private suspend fun persistForRetry(slug: String, records: JsonArray) {
        pendingSyncBatchDao.insert(
            PendingSyncBatch(
                type = slug,
                recordsJson = records.toString()
            )
        )
    }
}
