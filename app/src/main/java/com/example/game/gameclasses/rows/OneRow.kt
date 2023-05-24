package com.example.game.gameclasses.rows

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
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
    private val setDirection: Boolean,
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
    private val imageView6: View =
        rowBinding.findViewById(R.id.image6)
    private val imageView7: View =
        rowBinding.findViewById(R.id.image7)

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
    private val image6Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView6)
    private val image7Binding: ViewDataBinding? =
        DataBindingUtil.bind(imageView7)

    private val _isPlay = MutableStateFlow(false)
    val isPlay: StateFlow<Boolean> = _isPlay.asStateFlow()

    private val _isStop = MutableStateFlow(0)
    val isStop: StateFlow<Int> = _isStop.asStateFlow()

    private var stopPlay = false

    init {
        shiftList()
    }

    fun startPlay(order: Int, speed: Int) {
        var shift = imageView4.height
        if(slide) {
            val layoutParams = rowBinding.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = -2*shift
            layoutParams.bottomMargin = -2*shift
            rowBinding.requestLayout()
            imageView1.visibility = VISIBLE
            imageView2.visibility = VISIBLE
            imageView6.visibility = VISIBLE
            imageView7.visibility = VISIBLE
            if (setDirection) {
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
                        if (i < 4) {
                            120L
                        //} else if (i < 6) {
                        //    80L
                        } else {
                            35L + speed * 2L
                        }
                    val animatorSet = makeAnimation(delayShift, shift)
                    delay(delayShift + 10L)
                    animatorSet.cancel()
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

    private fun stopPlay() {
        _isPlay.value = false
        _isStop.value = listImages[3].id.toInt()
    }

    private fun setImages() {

        val list = if(setDirection) {
            listImages
        } else {
            listImages.reversed()
        }

        image1Binding?.setVariable(BR.itemImage,list[0].image)
        imageView1.translationY = 0F
        image2Binding?.setVariable(BR.itemImage,list[1].image)
        imageView2.translationY = 0F
        image3Binding?.setVariable(BR.itemImage,list[2].image)
        imageView3.translationY = 0F
        image4Binding?.setVariable(BR.itemImage,list[3].image)
        imageView4.translationY = 0F
        image5Binding?.setVariable(BR.itemImage,list[4].image)
        imageView5.translationY = 0F
        image6Binding?.setVariable(BR.itemImage,list[5].image)
        imageView6.translationY = 0F
        image7Binding?.setVariable(BR.itemImage,list[6].image)
        imageView7.translationY = 0F
    }

    private fun shiftList() {
        val first = listImages[0]
        listImages.removeAt(0)
        listImages.add(first)
        setImages()
    }

    private fun makeAnimation(delayShift: Long, shift: Int): AnimatorSet {
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
        val animation6 = ObjectAnimator.ofFloat(
            imageView6,
            "translationY",
            imageView6.translationY + shift
        )
        val animation7 = ObjectAnimator.ofFloat(
            imageView7,
            "translationY",
            imageView7.translationY + shift
        )
        animation1.duration = delayShift
        animation2.duration = delayShift
        animation3.duration = delayShift
        animation4.duration = delayShift
        animation5.duration = delayShift
        animation6.duration = delayShift
        animation7.duration = delayShift

        val interpolator = AccelerateDecelerateInterpolator()
        animation1.interpolator = interpolator
        animation2.interpolator = interpolator
        animation3.interpolator = interpolator
        animation4.interpolator = interpolator
        animation5.interpolator = interpolator
        animation6.interpolator = interpolator
        animation7.interpolator = interpolator

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            animation1,
            animation2,
            animation3,
            animation4,
            animation5,
            animation6,
            animation7,
        )
        animatorSet.start()
        return animatorSet
    }

}