package com.example.game.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

object ScreenStatus {

    fun setScreenStatus(activity: Activity, unblock: Boolean) {
        activity.requestedOrientation =
        if(unblock) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            val orientation = activity.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

}