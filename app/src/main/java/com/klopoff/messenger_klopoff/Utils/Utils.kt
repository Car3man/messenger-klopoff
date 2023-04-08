package com.klopoff.messenger_klopoff.Utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils {
    companion object {
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.rootView.windowToken, 0)
            }
        }

        fun isPointInsideView(x: Int, y: Int, view: View): Boolean {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val viewX = location[0]
            val viewY = location[1]
            return x > viewX && x < viewX + view.width && y > viewY && y < viewY + view.height
        }
    }
}
