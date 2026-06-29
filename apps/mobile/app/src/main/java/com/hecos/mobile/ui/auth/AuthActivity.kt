package com.hecos.mobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hecos.mobile.R
import com.hecos.mobile.data.api.ApiClient
import com.hecos.mobile.data.repository.TokenStore
import com.hecos.mobile.databinding.ActivityAuthBinding
import com.hecos.mobile.ui.home.HomeActivity
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var tokenStore: TokenStore

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authenticateWithBackend(idToken)
            } else {
                showError("No se obtuvo el token de Google")
            }
        } catch (e: ApiException) {
            showError("Error de Google Sign-In: ${e.statusCode}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenStore = TokenStore(this)

        lifecycleScope.launch {
            if (tokenStore.hasSession()) {
                navigateToHome()
                return@launch
            }
        }

        binding.btnSignIn.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(this, gso)
        signInLauncher.launch(client.signInIntent)
    }

    private fun authenticateWithBackend(idToken: String) {
        binding.btnSignIn.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = ApiClient.api.authGoogle(mapOf("idToken" to idToken))
                if (response.isSuccessful) {
                    val body = response.body()!!
                    tokenStore.saveSession(body.token, body.email, body.name)
                    navigateToHome()
                } else {
                    showError("Error de autenticación: ${response.code()}")
                    resetUi()
                }
            } catch (e: Exception) {
                showError("Error de conexión: ${e.message}")
                resetUi()
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun resetUi() {
        binding.btnSignIn.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
