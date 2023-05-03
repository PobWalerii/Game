package com.example.game.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class GameRepository @Inject constructor() {

    private val _gamerBalance = MutableStateFlow(0)
    val gamerBalance: StateFlow<Int> = _gamerBalance.asStateFlow()

    init {
        _gamerBalance.value = 10000
    }

    fun setGamerBalance(addSumm: Int) {
        _gamerBalance.value = gamerBalance.value + addSumm
    }


}