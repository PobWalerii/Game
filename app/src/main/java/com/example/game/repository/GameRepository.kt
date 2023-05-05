package com.example.game.repository

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.game.R
import com.example.game.constants.GamesConstants.DELTA_CHANGE_RATE_GAME1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val context: Context
) {

    private val _gamerBalance = MutableStateFlow(0)
    val gamerBalance: StateFlow<Int> = _gamerBalance.asStateFlow()

    private val _gamerRateGame1 = MutableStateFlow(0)
    val gamerRateGame1: StateFlow<Int> = _gamerRateGame1.asStateFlow()

    private val _listWheel1 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel1: Flow<List<WheelImages>> = _listWheel1.asStateFlow()

    private val _listWheel2 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel2: Flow<List<WheelImages>> = _listWheel2.asStateFlow()

    private val _listWheel3 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel3: Flow<List<WheelImages>> = _listWheel3.asStateFlow()

    private val list_images = listOf(
        ContextCompat.getDrawable(context, R.drawable.game1_slot1),
        ContextCompat.getDrawable(context, R.drawable.game1_slot2),
        ContextCompat.getDrawable(context, R.drawable.game1_slot3),
        ContextCompat.getDrawable(context, R.drawable.game1_slot4),
        ContextCompat.getDrawable(context, R.drawable.game1_slot5),
        ContextCompat.getDrawable(context, R.drawable.game1_slot6),
        ContextCompat.getDrawable(context, R.drawable.game1_slot7),
    )

    private var wheel1 = mutableListOf<WheelImages>()
    private var wheel2 = mutableListOf<WheelImages>()
    private var wheel3 = mutableListOf<WheelImages>()

    init {
        _gamerBalance.value = 10000
        initWheels()
    }

    private fun initWheels() {
        for(i in 1..3) {
            val list = when (i) {
                1 -> wheel1
                2 -> wheel2
                else -> wheel3
            }
            val random = Random()
            var startImage = random.nextInt(list_images.size)
            for (j in 1..list_images.size) {
                list.add(WheelImages(j.toLong(), list_images[startImage]!!))
                startImage++
                if (startImage == list_images.size) {
                    startImage = 0
                }
            }
            setFlowListWheel(i, list)
        }
    }



    private fun setFlowListWheel(i: Int, list: List<WheelImages>) {
        val flowList = when (i) {
            1 -> _listWheel1
            2 -> _listWheel2
            else -> _listWheel3
        }
        flowList.value = emptyList()
        flowList.value = list.reversed()
    }

    fun changePosition(wheel: Int) {
        val list = when (wheel) {
            1 -> wheel1
            2 -> wheel2
            else -> wheel3
        }
        val first = list[0]
        list.removeAt(0)
        list.add(first)
        setFlowListWheel(wheel, list)
    }

    fun startRateGame1() {
        _gamerRateGame1.value =
            if (gamerBalance.value < DELTA_CHANGE_RATE_GAME1) {
                gamerBalance.value
            } else {
                DELTA_CHANGE_RATE_GAME1
            }
    }

    fun changeRateGame1(plus: Boolean) {
        val newValue = gamerRateGame1.value +
                if (plus) {
                    DELTA_CHANGE_RATE_GAME1
                } else {
                    -DELTA_CHANGE_RATE_GAME1
                }
        _gamerRateGame1.value =
            if (newValue <= 0) {
                0
            } else if (newValue <= gamerBalance.value) {
                newValue
            } else {
                gamerBalance.value
            }
    }

    fun setGamerBalance(addSum: Int) {
        val newBalance = gamerBalance.value + addSum
        _gamerBalance.value = newBalance

        if(gamerRateGame1.value < newBalance) {
            _gamerRateGame1.value = newBalance
        }
    }


}

data class WheelImages (
    val id: Long,
    val image: Drawable
)