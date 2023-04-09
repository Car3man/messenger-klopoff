package com.klopoff.messenger_klopoff.Utils

import android.text.TextUtils

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isUsernameValid(): Boolean {
    return this.trim().length >= 6
}


fun String.isPasswordValid(): Boolean {
    return this.trim().length >= 8
}