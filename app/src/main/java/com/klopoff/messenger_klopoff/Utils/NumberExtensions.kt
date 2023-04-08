package com.klopoff.messenger_klopoff.Utils

import android.content.Context

fun Int.toDp(context: Context) : Int {
    val density = context.resources.displayMetrics.density
    return (this / density).toInt()
}

fun Int.toPx(context: Context) : Int {
    val density = context.resources.displayMetrics.density
    return (this * density).toInt()
}