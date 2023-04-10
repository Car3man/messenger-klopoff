package com.klopoff.messenger_klopoff.utils

import android.content.Context
import android.view.View

class UiUtils {
    companion object {
        fun toDp(context: Context, px: Int): Int {
            val density = context.resources.displayMetrics.density
            return (px / density).toInt()
        }

        fun toPx(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density).toInt()
        }

        fun isPointInsideView(view: View, x: Int, y: Int): Boolean {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val viewX = location[0]
            val viewY = location[1]
            return x > viewX && x < viewX + view.width && y > viewY && y < viewY + view.height
        }
    }
}