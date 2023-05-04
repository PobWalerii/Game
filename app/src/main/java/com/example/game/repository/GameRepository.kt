package com.example.game.repository

import com.example.game.constants.GamesConstants.DELTA_CHANGE_RATE_GAME1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class GameRepository @Inject constructor() {

    private val _gamerBalance = MutableStateFlow(0)
    val gamerBalance: StateFlow<Int> = _gamerBalance.asStateFlow()

    private val _gamerRateGame1 = MutableStateFlow(0)
    val gamerRateGame1: StateFlow<Int> = _gamerRateGame1.asStateFlow()

    init {
        _gamerBalance.value = 10000
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