package com.klopoff.messenger_klopoff.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

class InputUtils {
    companion object {
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.rootView.windowToken, 0)
            }
        }
    }
}