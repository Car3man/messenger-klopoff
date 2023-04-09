package com.klopoff.messenger_klopoff.Utils

import android.app.Activity
import android.view.MotionEvent

interface DispatchableTouchEventFragment {
    fun dispatchTouchEvent(activity: Activity, event: MotionEvent): Boolean
}