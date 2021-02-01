package com.martynov.tydchatand

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.martynov.tydchatand.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar!!.setSubtitle(R.string.authorization)
        if (isAuthenticated()) {
            navigateToFeed()
            return
        }



        binding.btnRegistration.setOnClickListener {
            navigateToRegistration()
        }
        binding.btnLogin.setOnClickListener {
            when {
                !isValidUsername(binding.loginText.text.toString()) -> {
                    binding.textInputLogin.error = getString(R.string.bad_login)
                }
                !isValidPassword(binding.passwordText.text.toString()) -> {
                    binding.textInputPassword.error = getString(R.string.bad_password)
                }
                else -> {
                    lifecycleScope.launch {
                        dialog = ProgressDialog(this@MainActivity).apply {
                            setMessage(getString(R.string.please_wait))
                            setTitle(getString(R.string.loading_data))
                            show()
                            setCancelable(false)
                        }
                        val login = binding.loginText.text?.toString().orEmpty()
                        val password = binding.passwordText.text?.toString().orEmpty()
                        try {
                            Log.d("My", "Вошел")
                            val token = App.repository.authenticate(login, password)
                            dialog?.dismiss()
                            if (token.isSuccessful) {
                                setUserAuth(requireNotNull(token.body()?.token))
                                requestToken()
                                navigateToFeed()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.authorisation_Error),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.d("My", e.toString())
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.falien_connect),
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog?.dismiss()
                        }
                    }
                }
            }

        }


    }

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .apply()

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@MainActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }

            if (isUserResolvableError(code)) {
                getErrorDialog(this@MainActivity, code, 9000).show()
                return
            }

            binding.root.longSnackbar(getString(R.string.google_play_unavailable))
            return
        }
    }

    private fun navigateToFeed() {
        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegistration() {
        val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isAuthenticated(): Boolean =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

}