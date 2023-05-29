package com.example.game.gameclasses.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

object ItemAnim {

    fun makeItemAnim(view: View?) {
        val anim = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f))
        anim.duration = 500L
        anim.start()
        val restoreAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", 1.0f),
            PropertyValuesHolder.ofFloat("scaleY", 1.0f),
        )
        restoreAnimator.startDelay = 500L
        restoreAnimator.duration = 500L
        restoreAnimator.start()

    }
}