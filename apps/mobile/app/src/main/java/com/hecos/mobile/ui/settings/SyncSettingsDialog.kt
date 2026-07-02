package com.hecos.mobile.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.switchmaterial.SwitchMaterial
import com.hecos.mobile.R
import com.hecos.mobile.data.repository.TokenStore
import com.hecos.mobile.work.AutoSyncScheduler
import kotlinx.coroutines.launch

/**
 * Simple settings dialog to enable/disable automatic sync and pick its frequency.
 */
object SyncSettingsDialog {

    fun show(context: Context, scope: LifecycleCoroutineScope, tokenStore: TokenStore) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_sync_settings, null)

        val switchAutoSync = view.findViewById<SwitchMaterial>(R.id.switchAutoSync)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupFrequency)

        fun radioIdForHours(hours: Int) = when (hours) {
            1 -> R.id.radioFreq1
            12 -> R.id.radioFreq12
            24 -> R.id.radioFreq24
            else -> R.id.radioFreq6
        }

        fun hoursForRadioId(id: Int) = when (id) {
            R.id.radioFreq1 -> 1
            R.id.radioFreq12 -> 12
            R.id.radioFreq24 -> 24
            else -> 6
        }

        scope.launch {
            switchAutoSync.isChecked = tokenStore.isAutoSyncEnabled()
            radioGroup.check(radioIdForHours(tokenStore.getAutoSyncIntervalHours()))
        }

        AlertDialog.Builder(context)
            .setTitle(R.string.auto_sync_settings)
            .setView(view)
            .setPositiveButton(R.string.save) { _, _ ->
                val enabled = switchAutoSync.isChecked
                val hours = hoursForRadioId(radioGroup.checkedRadioButtonId)

                scope.launch {
                    tokenStore.setAutoSyncEnabled(enabled)
                    tokenStore.setAutoSyncIntervalHours(hours)

                    if (enabled) {
                        AutoSyncScheduler.schedule(context, hours)
                    } else {
                        AutoSyncScheduler.cancel(context)
                    }
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
