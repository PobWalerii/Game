package com.example.game.gamesclasses.rows

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.game.R
import com.example.game.databinding.RowGame2Binding
import com.example.game.utils.RandomList.makeRandomList
import com.example.game.utils.Vibrator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RowsManager @Inject constructor(
    private val context: Context
) {

    private val _isPlay = MutableStateFlow(false)
    val isPlay: StateFlow<Boolean> = _isPlay.asStateFlow()

    private val _playResult = MutableStateFlow("")
    val playResult: StateFlow<String> = _playResult.asStateFlow()

    private lateinit var row1: OneRow
    private lateinit var row2: OneRow
    private lateinit var row3: OneRow

    private var firstPlay = false
    private var allPlay = false

    fun init(
        row1Binding: RowGame2Binding,
        row2Binding: RowGame2Binding,
        row3Binding: RowGame2Binding,
        lifecycleOwner: LifecycleOwner,
    ) {

        val listImages = listOf(
            R.drawable.game2_img1,
            R.drawable.game2_img2,
            R.drawable.game2_img3,
        )

        row1 = OneRow(row1Binding, makeRandomList(listImages), lifecycleOwner)
        row2 = OneRow(row2Binding, makeRandomList(listImages), lifecycleOwner)
        row3 = OneRow(row3Binding, makeRandomList(listImages), lifecycleOwner)

        lifecycleOwner.lifecycleScope.launch {
            combine(row1.isPlay, row2.isPlay, row3.isPlay) { isRt1, isRt2, isRt3 ->
                Triple(isRt1, isRt2, isRt3)
            }.collect { (isRt1, isRt2, isRt3) ->
                if(isRt1 || isRt2 || isRt3) {
                    firstPlay = true
                    _isPlay.value = true
                } else {
                    _isPlay.value = false
                }
                if(isRt1 && isRt2 && isRt3) {
                    allPlay = true
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            combine(row1.isStop, row2.isStop, row3.isStop) { isSt1, isSt2, isSt3 ->
                Triple(isSt1, isSt2, isSt3)
            }.collect { (isSt1, isSt2, isSt3) ->
                if (isSt1 !=0 || isSt2 !=0 || isSt3 !=0) {
                    if(allPlay) {
                        allPlay = false
                        row1.stopAll()
                        row2.stopAll()
                        row3.stopAll()
                    }
                    if (firstPlay) {
                        Vibrator.startVibrator(context)
                    }
                }
                if (isSt1 !=0 && isSt2 !=0 && isSt3 !=0) {
                    _playResult.value = when {
                        isSt1 == isSt2 && isSt2 == isSt3 -> "x5"
                        isSt1 == isSt2 || isSt1 == isSt3 || isSt2 == isSt3 -> "x2"
                        else -> "--"
                    }
                    _isPlay.value = false
                }
            }
        }
    }

    fun startPlay() {
        _playResult.value = ""
        val startOrder = mutableListOf(0,1,2)
        startOrder.shuffle()
        val speed = mutableListOf(5,7,9)
        speed.shuffle()
        row1.startPlay(startOrder[0],speed[0])
        row2.startPlay(startOrder[1],speed[1])
        row3.startPlay(startOrder[2],speed[2])
    }

}