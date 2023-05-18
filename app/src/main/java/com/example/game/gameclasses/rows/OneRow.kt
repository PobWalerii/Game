package com.example.game.gameclasses.rows

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.game.R
import com.example.game.constants.GamesConstants.DELAY_START_ROW_INTERVAL
import com.example.game.data.ItemImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class OneRow (
    private val rowBinding: View,
    private val listImages: MutableList<ItemImages>,
    private val direction: Int,
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

    private var stopPlay = false

    private var setDirection = true

    init {
        setImages()
    }

    fun startPlay(order: Int, speed: Int) {
        val shift = imageView3.height
        if(slide) {
            val layoutParams = rowBinding.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = -shift
            layoutParams.bottomMargin = -shift
            rowBinding.requestLayout()
            imageView1.visibility = VISIBLE
            imageView5.visibility = VISIBLE
        }
        _isStop.value = 0
        _isPlay.value = true
        stopPlay = false
        setDirection =
            if (direction == 3) {
                val dir = mutableListOf(true, false)
                dir.shuffle()
                dir[0]
            } else {
                direction == 1
            }

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(DELAY_START_ROW_INTERVAL *order.toLong())
            var delayShift = 1000L
            var i = 0
            while(i < 50) {
                shiftList()
                if(slide) {
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
                    animation1.duration = delayShift
                    animation2.duration = delayShift
                    animation3.duration = delayShift
                    animation4.duration = delayShift
                    animation5.duration = delayShift

                    val interpolator = AccelerateDecelerateInterpolator()
                    animation1.interpolator = interpolator
                    animation2.interpolator = interpolator
                    animation3.interpolator = interpolator
                    animation4.interpolator = interpolator
                    animation5.interpolator = interpolator
                    val animatorSet = AnimatorSet()
                    animatorSet.playTogether(animation1, animation2, animation3, animation4, animation5)

                    animation5.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                        }
                        override fun onAnimationEnd(animation: Animator) {
                            i++
                        }
                        override fun onAnimationCancel(animation: Animator) {
                        }
                        override fun onAnimationRepeat(animation: Animator) {
                        }
                    })


                    animatorSet.start()
                    //delay(delayShift)

                } else {
                    delay(20 * speed.toLong())
                    i++
                }

                if(stopPlay) {
                    break
                }
            }
            stopPlay()
        }
    }

    fun stopAll() {
        stopPlay = true
    }

    private fun stopPlay() {
        _isPlay.value = false
        _isStop.value = listImages[2].id.toInt()
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
    }

    private fun shiftList() {
        val first = listImages[0]
        listImages.removeAt(0)
        listImages.add(first)
        setImages()
    }


}