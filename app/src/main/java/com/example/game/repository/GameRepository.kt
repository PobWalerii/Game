package com.example.game.repository

import android.content.Context
import com.example.game.R
import com.example.game.constants.GamesConstants.DELTA_CHANGE_RATE_GAME1
import com.example.game.constants.GamesConstants.DELTA_CHANGE_RATE_GAME2
import com.example.game.wheels.WheelsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val wheelsManager: WheelsManager,
    private val context: Context
) {

    private val _gamerBalance = MutableStateFlow(0)
    val gamerBalance: StateFlow<Int> = _gamerBalance.asStateFlow()

    private val _gamerRateGame1 = MutableStateFlow(0)
    val gamerRateGame1: StateFlow<Int> = _gamerRateGame1.asStateFlow()

    private val _gamerRateGame2 = MutableStateFlow(0)
    val gamerRateGame2: StateFlow<Int> = _gamerRateGame2.asStateFlow()

    private val _listWheel1Game1 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel1Game1: Flow<List<WheelImages>> = _listWheel1Game1.asStateFlow()

    private val _listWheel2Game1 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel2Game1: Flow<List<WheelImages>> = _listWheel2Game1.asStateFlow()

    private val _listWheel3Game1 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel3Game1: Flow<List<WheelImages>> = _listWheel3Game1.asStateFlow()

    private val _listWheel1Game2 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel1Game2: Flow<List<WheelImages>> = _listWheel1Game2.asStateFlow()

    private val _listWheel2Game2 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel2Game2: Flow<List<WheelImages>> = _listWheel2Game2.asStateFlow()

    private val _listWheel3Game2 = MutableStateFlow(emptyList<WheelImages>())
    val listWheel3Game2: Flow<List<WheelImages>> = _listWheel3Game2.asStateFlow()






    private val listImagesGame1 = listOf(
        R.drawable.game1_slot1,
        R.drawable.game1_slot2,
        R.drawable.game1_slot3,
        R.drawable.game1_slot4,
        R.drawable.game1_slot5,
        R.drawable.game1_slot6,
        R.drawable.game1_slot7
    )
    private val listImagesGame2 = listOf(
        R.drawable.game1_slot1,
        R.drawable.game1_slot2,
        R.drawable.game1_slot3,
        R.drawable.game1_slot4,
        R.drawable.game1_slot5,
    )

    private var wheel1Game1 = mutableListOf<WheelImages>()
    private var wheel2Game1 = mutableListOf<WheelImages>()
    private var wheel3Game1 = mutableListOf<WheelImages>()

    private var wheel1Game2 = mutableListOf<WheelImages>()
    private var wheel2Game2 = mutableListOf<WheelImages>()
    private var wheel3Game2 = mutableListOf<WheelImages>()

    private val wheelsGame1 = listOf(wheel1Game1,wheel2Game1,wheel3Game1)
    private val wheelsGame2 = listOf(wheel1Game2,wheel2Game2,wheel3Game2)
    private val flowListWheelsGame1 = listOf(_listWheel1Game1,_listWheel2Game1,_listWheel3Game1)
    private val flowListWheelsGame2 = listOf(_listWheel1Game2,_listWheel2Game2,_listWheel3Game2)

    val wheelChangePosition1: StateFlow<Int> = wheelsManager.wheelChangePosition1
    val wheelChangePosition2: StateFlow<Int> = wheelsManager.wheelChangePosition2
    val wheelChangePosition3: StateFlow<Int> = wheelsManager.wheelChangePosition3
    val gameNumber: StateFlow<Int> = wheelsManager.gameNumber

    init {
        _gamerBalance.value = 10000
        initWheels()
        startRateGame()
        observeWheelChangePosition()
    }

    private fun initWheels() {
        for(game in 1..2) {
            val listImages = if(game==1) {
                listImagesGame1
            } else {
                listImagesGame2
            }
            val wheels = if(game==1) {
                wheelsGame1
            } else {
                wheelsGame2
            }
            for (i in 1..3) {
                val list = wheels[i-1]
                val random = Random()
                var startImage = random.nextInt(listImages.size)
                for (j in 1..listImages.size) {
                    list.add(WheelImages(j.toLong(), listImages[startImage]))
                    startImage++
                    if (startImage == listImages.size) {
                        startImage = 0
                    }
                }
                setFlowListWheel(game, i, list)
            }
        }
    }

    private fun setFlowListWheel(game: Int, wheel: Int, list: List<WheelImages>) {
        val flowList = if(game==1) {
            flowListWheelsGame1
        } else {
            flowListWheelsGame2
        }[wheel-1]
        flowList.value = emptyList()
        flowList.value = list.reversed()
    }

    private fun observeWheelChangePosition() {
        CoroutineScope(Dispatchers.Default).launch {
            wheelChangePosition1.collect {
                if(it != 0) {
                    changePosition(gameNumber.value, it)
                }
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            wheelChangePosition2.collect {
                if(it != 0) {
                    changePosition(gameNumber.value, it)
                }
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            wheelChangePosition3.collect {
                if(it != 0) {
                    changePosition(gameNumber.value, it)
                }
            }
        }
    }

    private fun changePosition(game: Int, wheel: Int) {
        val list = if(game==1) {
            wheelsGame1
        } else {
            wheelsGame2
        }[wheel-1]

        val first = list[0]
        list.removeAt(0)
        list.add(first)
        setFlowListWheel(game, wheel, list)
    }

    fun startRateGame() {
        _gamerRateGame1.value =
            if (gamerBalance.value < DELTA_CHANGE_RATE_GAME1) {
                gamerBalance.value
            } else {
                DELTA_CHANGE_RATE_GAME1
            }
        _gamerRateGame2.value =
            if (gamerBalance.value < DELTA_CHANGE_RATE_GAME2) {
                gamerBalance.value
            } else {
                DELTA_CHANGE_RATE_GAME2
            }
    }

    fun changeRateGame(game: Int, plus: Boolean) {
        val delta = if (game == 1) {
            DELTA_CHANGE_RATE_GAME1
        } else {
            DELTA_CHANGE_RATE_GAME2
        }
        val newValue = if (game == 1) {
            gamerRateGame1.value
        } else {
            gamerRateGame2.value
        } + if (plus) {
            delta
        } else {
            -delta
        }
        val flowValue = if (game == 1) {
            _gamerRateGame1
        } else {
            _gamerRateGame2
        }
        flowValue.value =
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

