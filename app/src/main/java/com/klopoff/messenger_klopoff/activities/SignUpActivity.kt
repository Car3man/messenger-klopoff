package com.klopoff.messenger_klopoff.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.databinding.ActivitySignupBinding
import com.klopoff.messenger_klopoff.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { handleRegisterButton() }
        binding.btnSignUp.setOnClickListener { handleSignInButton() }
    }

    private fun handleRegisterButton() {
        binding.tfEmail.error = null
        binding.tfUsername.error = null
        binding.tfPassword.error = null
        binding.tfPasswordConfirm.error = null
        binding.tvError.text = ""

        val email = binding.tfEmail.editText!!.text.toString()
        val username = binding.tfUsername.editText!!.text.toString()
        val password = binding.tfPassword.editText!!.text.toString()
        val passwordConfirm = binding.tfPasswordConfirm.editText!!.text.toString()

        if (!ValidationUtils.validateEmail(email)) {
            binding.tfEmail.error = getString(R.string.invalid_email_error)
            return
        }

        if (!ValidationUtils.validateUsername(username)) {
            binding.tfUsername.error = getString(R.string.invalid_username_error)
            return
        }

        if (!ValidationUtils.validatePassword(password)) {
            binding.tfPassword.error = getString(R.string.invalid_password_error)
            return
        }

        if (password != passwordConfirm) {
            binding.tfPasswordConfirm.error = getString(R.string.invalid_password_confirm_error)
            return
        }

        lifecycleScope.launch {
            val authTask = auth.createUserWithEmailAndPassword(email, password)

            try {
                authTask.await()
                val setDisplayNameRequest = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                auth.currentUser!!.updateProfile(setDisplayNameRequest).await()
            } catch (exception: Exception) {
                Log.e(SignUpActivity::class.java.name, "Sign up exception: ${exception.message}")
            }

            if (authTask.isSuccessful) {
                val user = authTask.result.user
                database.reference
                    .child("persons")
                    .child(user!!.uid)
                    .setValue(object {
                        val userId = user.uid
                        val userName = user.displayName
                    })
                    .await()
            }

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