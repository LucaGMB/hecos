package com.hecos.mobile.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hecos.mobile.data.health.HealthConnectReader
import com.hecos.mobile.data.local.HecosDatabase
import com.hecos.mobile.data.repository.SyncService
import com.hecos.mobile.data.repository.TokenStore
import com.hecos.mobile.databinding.ActivityHomeBinding
import com.hecos.mobile.ui.auth.AuthActivity
import com.hecos.mobile.ui.settings.SyncSettingsDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var tokenStore: TokenStore
    private lateinit var reader: HealthConnectReader
    private lateinit var syncService: SyncService

    private val permissionLauncher = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) {
        lifecycleScope.launch { checkPermissions() }
    }

    private val activityRecognitionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        permissionLauncher.launch(HealthConnectReader.PERMISSIONS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenStore = TokenStore(this)

        if (!HealthConnectReader.isAvailable(this)) {
            Toast.makeText(this, "Health Connect no está disponible", Toast.LENGTH_LONG).show()
            return
        }

        reader = HealthConnectReader(this)
        val pendingSyncBatchDao = HecosDatabase.getInstance(this).pendingSyncBatchDao()
        syncService = SyncService(reader, tokenStore, pendingSyncBatchDao)

        lifecycleScope.launch {
            binding.tvUserEmail.text = tokenStore.getUserEmail() ?: ""
        }

        binding.btnSignOut.setOnClickListener { signOut() }
        binding.btnGrantPermissions.setOnClickListener { requestPermissions() }
        binding.btnSync.setOnClickListener { startSync() }
        binding.btnSyncSettings.setOnClickListener { showSyncSettings() }

        lifecycleScope.launch { checkPermissions() }
        observeSyncProgress()
    }

    private fun showSyncSettings() {
        SyncSettingsDialog.show(this, lifecycleScope, tokenStore)
    }

    private suspend fun checkPermissions() {
        val hasPerms = reader.hasAllPermissions()
        binding.cardPermissions.visibility = if (hasPerms) View.GONE else View.VISIBLE
        binding.btnSync.isEnabled = hasPerms
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
        ) {
            activityRecognitionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            permissionLauncher.launch(HealthConnectReader.PERMISSIONS)
        }
    }

    private fun startSync() {
        binding.btnSync.isEnabled = false
        binding.syncProgress.visibility = View.VISIBLE
        binding.tvSyncDetails.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                syncService.syncAll()
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                binding.btnSync.isEnabled = true
                binding.syncProgress.visibility = View.GONE
            }
        }
    }

    private fun observeSyncProgress() {
        lifecycleScope.launch {
            syncService.progress.collectLatest { progress ->
                if (progress.typesTotal == 0) return@collectLatest

                binding.syncProgress.max = progress.typesTotal
                binding.syncProgress.progress = progress.typesCompleted

                if (progress.isComplete) {
                    binding.tvSyncStatus.text = "Sincronización completada"
                    binding.tvSyncDetails.text = buildString {
                        append("${progress.totalSaved} registros guardados")
                        if (progress.totalDuplicates > 0) {
                            append(", ${progress.totalDuplicates} duplicados omitidos")
                        }
                        if (progress.errors.isNotEmpty()) {
                            append("\n${progress.errors.size} errores")
                        }
                    }
                    binding.btnSync.isEnabled = true
                    binding.btnSync.text = "Sincronizar de nuevo"
                } else {
                    binding.tvSyncStatus.text = "Sincronizando…"
                    binding.tvSyncDetails.text = "${progress.currentType} (${progress.typesCompleted}/${progress.typesTotal})"
                }
            }
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            tokenStore.clear()
            GoogleSignIn.getClient(
                this@HomeActivity,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()
            startActivity(Intent(this@HomeActivity, AuthActivity::class.java))
            finish()
        }
    }
}
