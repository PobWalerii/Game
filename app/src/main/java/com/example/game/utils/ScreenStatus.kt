package com.example.game.utils

import android.app.Activity
import android.content.pm.ActivityInfo

object ScreenStatus {

    fun setScreenStatus(activity: Activity, unblock: Boolean) {
        activity.requestedOrientation =
            if (unblock) {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            } else {
                ActivityInfo.SCREEN_ORIENTATION_LOCKED
            }
    }

}