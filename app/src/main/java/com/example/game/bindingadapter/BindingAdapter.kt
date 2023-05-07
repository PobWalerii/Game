package com.example.game.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("showImage")
    fun showImages(view: ImageView, image: Int) {
        Glide.with(view)
            .load(image)
            .into(view)
    }

}