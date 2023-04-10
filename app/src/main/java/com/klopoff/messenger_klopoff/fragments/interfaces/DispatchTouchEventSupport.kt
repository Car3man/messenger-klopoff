package com.klopoff.messenger_klopoff.fragments.interfaces

import android.app.Activity
import android.view.MotionEvent

interface DispatchTouchEventSupport {
    fun dispatchTouchEvent(activity: Activity, event: MotionEvent): Boolean
}