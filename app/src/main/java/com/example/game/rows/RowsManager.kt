package com.example.game.rows

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.game.R
import com.example.game.databinding.RowGame2Binding
import com.example.game.dataclass.ItemImages
import com.example.game.utils.Vibrator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*
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

        row1 = OneRow(row1Binding, getRandomList(listImages), lifecycleOwner)
        row2 = OneRow(row2Binding, getRandomList(listImages), lifecycleOwner)
        row3 = OneRow(row3Binding, getRandomList(listImages), lifecycleOwner)

        lifecycleOwner.lifecycleScope.launch {
            combine(row1.isPlay, row2.isPlay, row3.isPlay) { isRt1, isRt2, isRt3 ->
                isRt1 || isRt2 || isRt3
            }.collect {
                _isPlay.value = it
                if (it) {
                    firstPlay = true
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            combine(row1.isStop, row2.isStop, row3.isStop) { isSt1, isSt2, isSt3 ->
                Triple(isSt1, isSt2, isSt3)
            }.collect { (isSt1, isSt2, isSt3) ->
                if (isSt1 !=0 || isSt2 !=0 || isSt3 !=0) {
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
        row1.startPlay(startOrder[0])
        row2.startPlay(startOrder[1])
        row3.startPlay(startOrder[2])
    }

    private fun getRandomList(listImages: List<Int>): MutableList<ItemImages> {
        val list = mutableListOf<ItemImages>()
        repeat(listImages.size) { id ->
            list.add(ItemImages((id+1).toLong(), listImages[id]))
        }
        list.shuffle()
        return list
    }

}