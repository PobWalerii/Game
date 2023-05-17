package com.example.game.gamesclasses.rows

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.game.R
import com.example.game.constants.GamesConstants.DELAY_START_ROW_INTERVAL
import com.example.game.dataclass.ItemImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class OneRow (
    rowBinding: View,
    private val list: MutableList<ItemImages>,
    private val lifecycleOwner: LifecycleOwner,
) {

    private val image1Binding: ViewDataBinding? =
        DataBindingUtil.bind(rowBinding.findViewById(R.id.image1))
    private val image2Binding: ViewDataBinding? =
        DataBindingUtil.bind(rowBinding.findViewById(R.id.image2))
    private val image3Binding: ViewDataBinding? =
        DataBindingUtil.bind(rowBinding.findViewById(R.id.image3))

    private val _isPlay = MutableStateFlow(false)
    val isPlay: StateFlow<Boolean> = _isPlay.asStateFlow()

    private val _isStop = MutableStateFlow(0)
    val isStop: StateFlow<Int> = _isStop.asStateFlow()
    private var stopPlay = false



    init {
        setImages()
    }

    fun startPlay(order: Int, speed: Int) {

        _isStop.value = 0
        _isPlay.value = true
        stopPlay = false

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(DELAY_START_ROW_INTERVAL *order.toLong())

            for(i in 1..50) {
                withContext(Dispatchers.Main){
                    shiftList()
                }
                delay(20*speed.toLong())
                if(stopPlay) {
                    break
                }
            }

            withContext(Dispatchers.Main){
                stopPlay()
            }
        }
    }

    fun stopAll() {
        stopPlay = true
    }

    private fun stopPlay() {
        _isPlay.value = false
        _isStop.value = list[1].id.toInt()
    }

    private fun setImages() {
        image1Binding?.setVariable(BR.itemImage,list[0].image)
        image2Binding?.setVariable(BR.itemImage,list[1].image)
        image3Binding?.setVariable(BR.itemImage,list[2].image)
    }

    private fun shiftList() {
        val first = list[0]
        list.removeAt(0)
        list.add(first)
        setImages()
    }




}