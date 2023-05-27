package com.example.game.gameclasses.rows

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.game.R
import com.example.game.constants.GamesConstants.DELAY_START_ROW_INTERVAL
import com.example.game.data.ItemImages
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OneRow (
    private val rowBinding: View,
    private val listImages: MutableList<ItemImages>,
    private val setDirection: Boolean?,
    private val slide: Boolean,
    private val lifecycleOwner: LifecycleOwner,
) {

    private val imageView1: View =
        rowBinding.findViewById(R.id.image1)
    private val imageView2: View =
        rowBinding.findViewById(R.id.image2)
    private val imageView3: View =
        rowBinding.findViewById(R.id.image3)
    private val imageView4: View =
        rowBinding.findViewById(R.id.image4)
    private val imageView5: View =
        rowBinding.findViewById(R.id.image5)

    private val image1Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView1)
    private val image2Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView2)
    private val image3Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView3)
    private val image4Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView4)
    private val image5Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView5)

    private val _isPlay = MutableStateFlow(false)
    val isPlay: StateFlow<Boolean> = _isPlay.asStateFlow()

    private val _isStop = MutableStateFlow(0)
    val isStop: StateFlow<Int> = _isStop.asStateFlow()

    private var direction = false

    private var stopPlay = false

    init {
        shiftList()
    }

    fun startPlay(order: Int, speed: Int) {
        val dir = mutableListOf(false,true,false,true,false,true)
        dir.shuffle()
        direction = setDirection ?: dir[0]
        var shift = imageView3.height
        if(slide) {
            val layoutParams = rowBinding.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = -shift
            layoutParams.bottomMargin = -shift
            rowBinding.requestLayout()
            imageView1.visibility = VISIBLE
            imageView5.visibility = VISIBLE

            if (direction) {
                shift = -shift
            }
        }
        _isStop.value = 0
        _isPlay.value = true
        stopPlay = false

        lifecycleOwner.lifecycleScope.launch {
            delay(DELAY_START_ROW_INTERVAL * order.toLong())
            for (i in 1..50) {
                if (i != 1) {
                    shiftList()
                }
                if (slide) {
                    val delayShift =
                        if (i < 2 || i>49) {
                            100L
                        } else {
                            15L + speed * 5L
                        }
                    makeAnimation(delayShift, shift)
                    delay(delayShift)
                } else {
                    delay(20 * speed.toLong())
                }
                if (stopPlay) {
                    break
                }
            }
            delay(100)
            shiftList()
            stopPlay()
        }
    }

    fun stopAll(delay: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(DELAY_START_ROW_INTERVAL * delay)
            stopPlay = true
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun stopPlay() {
        _isPlay.value = false
        _isStop.value = listImages[listImages.size/2].id.toInt()
    }

    fun finishAnim() {
        val anim = ObjectAnimator.ofPropertyValuesHolder(imageView3, PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f))
        anim.duration = 500L
        anim.start()
        val restoreAnimator = ObjectAnimator.ofPropertyValuesHolder(
            imageView3,
            PropertyValuesHolder.ofFloat("scaleX", 1.0f),
            PropertyValuesHolder.ofFloat("scaleY", 1.0f),
        )
        restoreAnimator.startDelay = 500L
        restoreAnimator.duration = 500L
        restoreAnimator.start()
    }

    private fun setImages() {
        val centrPosition = listImages.size/2
        image1Binding?.setVariable(BR.itemImage,listImages[centrPosition-2].image)
        imageView1.translationY = 0F
        image2Binding?.setVariable(BR.itemImage,listImages[centrPosition-1].image)
        imageView2.translationY = 0F
        image3Binding?.setVariable(BR.itemImage,listImages[centrPosition].image)
        imageView3.translationY = 0F
        image4Binding?.setVariable(BR.itemImage,listImages[centrPosition+1].image)
        imageView4.translationY = 0F
        image5Binding?.setVariable(BR.itemImage,listImages[centrPosition+2].image)
        imageView5.translationY = 0F
    }

    private fun shiftList() {
        if (direction) {
            val first = listImages[0]
            listImages.removeAt(0)
            listImages.add(first)
        } else {
            val last = listImages.last()
            listImages.removeAt(listImages.size - 1)
            listImages.add(0, last)
        }
        setImages()
    }

    private fun makeAnimation(delayShift: Long, shift: Int) {
        val animation1 = ObjectAnimator.ofFloat(
            imageView1,
            "translationY",
            imageView1.translationY + shift
        )
        val animation2 = ObjectAnimator.ofFloat(
            imageView2,
            "translationY",
            imageView2.translationY + shift
        )
        val animation3 = ObjectAnimator.ofFloat(
            imageView3,
            "translationY",
            imageView3.translationY + shift
        )
        val animation4 = ObjectAnimator.ofFloat(
            imageView4,
            "translationY",
            imageView4.translationY + shift
        )
        val animation5 = ObjectAnimator.ofFloat(
            imageView5,
            "translationY",
            imageView5.translationY + shift
        )

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            animation1,
            animation2,
            animation3,
            animation4,
            animation5,
        )
        animatorSet.duration = delayShift
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }

}