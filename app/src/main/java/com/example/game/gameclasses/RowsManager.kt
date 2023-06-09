package com.example.game.gameclasses

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.data.GameCollection.getGame
import com.example.game.gameclasses.rowlist.OneRow
import com.example.game.gameclasses.rowrecycler.OneWheel
import com.example.game.gameclasses.utils.RowBase
import com.example.game.gameclasses.utils.RandomList.makeRandomList
import com.example.game.gameclasses.utils.Vibrator
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

    private lateinit var row1: RowBase
    private lateinit var row2: RowBase
    private lateinit var row3: RowBase

    private var firstPlay = false
    private var allPlay = false

    fun init(
        gameNumber: Int,
        containerBinding: View,
        lifecycleOwner: LifecycleOwner,
    ) {
        val gameSettings = getGame(gameNumber)
        val row1View: View = containerBinding.findViewById(R.id.row1)
        val row2View: View = containerBinding.findViewById(R.id.row2)
        val row3View: View = containerBinding.findViewById(R.id.row3)

        row1 = if(row1View is RecyclerView)  {
            OneWheel(row1View, makeRandomList(gameSettings.listImages), gameSettings.direction, lifecycleOwner)
        } else {
            OneRow(row1View, makeRandomList(gameSettings.listImages), gameSettings.direction, gameSettings.slide, lifecycleOwner)
        }
        row2 = if(row2View is RecyclerView)  {
            OneWheel(row2View, makeRandomList(gameSettings.listImages), gameSettings.direction, lifecycleOwner)
        } else {
            OneRow(row2View, makeRandomList(gameSettings.listImages), gameSettings.direction, gameSettings.slide, lifecycleOwner)
        }
        row3 = if(row3View is RecyclerView)  {
            OneWheel(row3View, makeRandomList(gameSettings.listImages), gameSettings.direction, lifecycleOwner)
        } else {
            OneRow(row3View, makeRandomList(gameSettings.listImages), gameSettings.direction, gameSettings.slide, lifecycleOwner)
        }

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
                if (isSt1 != 0 || isSt2 != 0 || isSt3 != 0) {
                    if (allPlay) {
                        allPlay = false
                        val stopTime = mutableListOf(1,2,3)
                        stopTime.shuffle()
                        row1.stopAll(stopTime[0])
                        row2.stopAll(stopTime[1])
                        row3.stopAll(stopTime[2])
                    }
                    if (firstPlay) {
                        Vibrator.startVibrator(context)
                    }
                }
                if (isSt1 !=0 && isSt2 !=0 && isSt3 !=0) {
                    row1.finishAnim()
                    row2.finishAnim()
                    row3.finishAnim()
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
        val speed = mutableListOf(3,5,7)
        speed.shuffle()
        row1.startPlay(startOrder[0],speed[0])
        row2.startPlay(startOrder[1],speed[1])
        row3.startPlay(startOrder[2],speed[2])
    }

}