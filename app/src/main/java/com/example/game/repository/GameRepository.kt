package com.example.game.repository

import android.content.Context
import com.example.game.constants.GamesConstants.DELTA_CHANGE_RATE_GAME
import com.example.game.constants.GamesConstants.PARTS_OF_BALANCE_SHEET
import com.example.game.constants.GamesConstants.START_GAMER_BALANCE
import com.example.game.wheels.WheelsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    wheelsManager: WheelsManager,
    private val context: Context
) {

    private val _gamerBalance = MutableStateFlow(0)
    val gamerBalance: StateFlow<Int> = _gamerBalance.asStateFlow()

    private val _rateGame = MutableStateFlow(0)
    val rateGame: StateFlow<Int> = _rateGame.asStateFlow()

    val isWheelsRotate: StateFlow<Boolean> = wheelsManager.isRotate

    private val gameResult: StateFlow<String> = wheelsManager.gameResult


    init {
        startGamerBalance()
        observeGameResult()
    }

    private fun observeGameResult() {
        CoroutineScope(Dispatchers.Main).launch {
            gameResult.collect { stringResult ->
                var delta = when (stringResult) {
                    "x2" -> rateGame.value*2
                    "x5" -> rateGame.value*5
                    "--" -> -rateGame.value
                    else -> 0
                }
                var newBalance = gamerBalance.value + delta
                if(newBalance<0) {
                    newBalance = 0
                }
                delta = newBalance - gamerBalance.value

                if(delta !=0) {
                    val shift = delta/(PARTS_OF_BALANCE_SHEET)
                    repeat(PARTS_OF_BALANCE_SHEET-1) {
                        delay(5)
                        _gamerBalance.value = gamerBalance.value + shift
                    }
                }
                _gamerBalance.value = newBalance
            }
        }
    }

    private fun startGamerBalance() {
        _gamerBalance.value = START_GAMER_BALANCE
        _rateGame.value = DELTA_CHANGE_RATE_GAME
    }

    fun changeRateGame(plus: Boolean) {
        val newValue = rateGame.value +
                if (plus) {
                    DELTA_CHANGE_RATE_GAME
                } else {
                    -DELTA_CHANGE_RATE_GAME
                }
        _rateGame.value =
            if (newValue <= 0) {
                0
            } else if (newValue <= gamerBalance.value) {
                newValue
            } else {
                gamerBalance.value
            }
    }


}

