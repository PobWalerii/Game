package com.example.game.gamesclasses.rows

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.game.constants.GamesConstants.DELAY_START_ROW_INTERVAL
import com.example.game.databinding.Game2RowBinding
import com.example.game.dataclass.ItemImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class OneRow (
    private val rowBinding: Game2RowBinding,
    private val list: MutableList<ItemImages>,
    private val lifecycleOwner: LifecycleOwner,
) {

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
        rowBinding.image1.itemImage = list[0].image
        rowBinding.image2.itemImage = list[1].image
        rowBinding.image3.itemImage = list[2].image
    }

    private fun shiftList() {
        val first = list[0]
        list.removeAt(0)
        list.add(first)
        setImages()
    }




}