package com.hecos.mobile.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Schedules/cancels the periodic background sync backed by [AutoSyncWorker].
 */
object AutoSyncScheduler {

    private const val WORK_NAME = "hecos_auto_sync"
    private const val BACKOFF_DELAY_MINUTES = 15L

    /** Frequencies offered to the user, in hours. WorkManager's periodic minimum is 15 min. */
    val AVAILABLE_INTERVAL_HOURS = listOf(1, 6, 12, 24)

    fun schedule(context: Context, intervalHours: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<AutoSyncWorker>(
            intervalHours.toLong(), TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_DELAY_MINUTES, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
