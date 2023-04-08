package com.klopoff.messenger_klopoff

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.klopoff.messenger_klopoff.HomeActivity.HomeActivity
import com.klopoff.messenger_klopoff.Utils.isEmailValid
import com.klopoff.messenger_klopoff.Utils.isPasswordValid
import com.klopoff.messenger_klopoff.databinding.ActivitySignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener { handleRegisterButton() }
        binding.btnSignUp.setOnClickListener { handleSignInButton() }
    }

    private fun handleRegisterButton() {
        binding.tfEmail.error = null
        binding.tfPassword.error = null
        binding.tfPasswordConfirm.error = null
        binding.tvError.text = ""

        val email = binding.tfEmail.editText!!.text.toString()
        val password = binding.tfPassword.editText!!.text.toString()
        val passwordConfirm = binding.tfPasswordConfirm.editText!!.text.toString()

        if (!email.isEmailValid()) {
            binding.tfEmail.error = getString(R.string.invalid_email_error)
            return
        }

        if (!password.isPasswordValid()) {
            binding.tfPassword.error = getString(R.string.invalid_password_error)
            return
        }

        if (password != passwordConfirm) {
            binding.tfPasswordConfirm.error = getString(R.string.invalid_password_confirm_error)
            return
        }

        lifecycleScope.launch {
            val authTask = auth.createUserWithEmailAndPassword(email, password)
            authTask.await()

            withContext(Dispatchers.Main) {
                if (authTask.isSuccessful) {
                    Intent(this@SignUpActivity, HomeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                } else {
                    binding.tvError.text = getString(R.string.something_went_wrong_error)
                }
            }
        }
    }

    private fun handleSignInButton() {
        Intent(this, SignInActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}