package com.hecos.mobile.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hecos.mobile.data.health.HealthConnectReader
import com.hecos.mobile.data.local.HecosDatabase
import com.hecos.mobile.data.repository.SyncService
import com.hecos.mobile.data.repository.TokenStore

/**
 * Runs a full sync (pending queue flush + new records) in the background,
 * scheduled periodically by [AutoSyncScheduler].
 */
class AutoSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            if (!HealthConnectReader.isAvailable(applicationContext)) {
                return Result.failure()
            }

            val tokenStore = TokenStore(applicationContext)
            if (!tokenStore.hasSession()) {
                return Result.failure()
            }

            val reader = HealthConnectReader(applicationContext)
            if (!reader.hasAllPermissions()) {
                return Result.failure()
            }

            val dao = HecosDatabase.getInstance(applicationContext).pendingSyncBatchDao()
            val syncService = SyncService(reader, tokenStore, dao)
            syncService.syncAll()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
