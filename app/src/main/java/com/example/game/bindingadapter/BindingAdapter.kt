package com.example.game.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("showImage")
    fun showImages(view: ImageView, image: Int) {
        view.setImageResource(image)
    }
}