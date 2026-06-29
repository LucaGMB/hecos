package com.hecos.mobile.data.repository

import com.hecos.mobile.data.api.ApiClient
import com.hecos.mobile.data.health.HealthConnectReader
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
    private val tokenStore: TokenStore
) {

    private val _progress = MutableStateFlow(SyncProgress())
    val progress: StateFlow<SyncProgress> = _progress

    suspend fun syncAll() {
        val token = tokenStore.getToken() ?: throw IllegalStateException("No token")
        val bearerToken = "Bearer $token"

        val allData = reader.readAll()
        val total = allData.size
        var completed = 0
        var totalSaved = 0
        var totalDuplicates = 0
        val errors = mutableListOf<String>()

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
                }
            } catch (e: Exception) {
                errors.add("$slug: ${e.message}")
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
}
