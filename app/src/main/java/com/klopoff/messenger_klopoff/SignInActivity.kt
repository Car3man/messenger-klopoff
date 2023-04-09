package com.klopoff.messenger_klopoff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.klopoff.messenger_klopoff.HomeActivity.HomeActivity
import com.klopoff.messenger_klopoff.Utils.isEmailValid
import com.klopoff.messenger_klopoff.Utils.isPasswordValid
import com.klopoff.messenger_klopoff.databinding.ActivitySigninBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { handleLoginButton() }
        binding.btnSignUp.setOnClickListener { handleSignUpButton() }
    }

    private fun handleLoginButton() {
        binding.tfEmail.error = null
        binding.tfPassword.error = null
        binding.tvError.text = ""

        val email = binding.tfEmail.editText!!.text.toString()
        val password = binding.tfPassword.editText!!.text.toString()

        if (!email.isEmailValid()) {
            binding.tfEmail.error = getString(R.string.invalid_email_error)
            return
        }

        if (!password.isPasswordValid()) {
            binding.tfPassword.error = getString(R.string.invalid_password_error)
            return
        }

        lifecycleScope.launch {
            val authTask = auth.signInWithEmailAndPassword(email, password)
            try {
                authTask.await()
            } catch (exception: Exception) {
                Log.e(SignInActivity::class.java.name, "Sign in exception: ${exception.message}")
            }

            withContext(Dispatchers.Main) {
                if (authTask.isSuccessful) {
                    Intent(this@SignInActivity, HomeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                } else {
                    binding.tvError.text = getString(R.string.invalid_credentials_error)
                }
            }
        }
    }

    private fun handleSignUpButton() {
        Intent(this, SignUpActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}