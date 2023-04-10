package com.klopoff.messenger_klopoff.utils

import android.util.Patterns

class ValidationUtils {
    companion object {
        fun validateEmail(email: String): Boolean {
            return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validateUsername(username: String): Boolean {
            return username.length >= 6
        }

        fun validatePassword(password: String): Boolean {
            return password.length >= 8
        }
    }
}