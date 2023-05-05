package com.example.game.ui.game1

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.example.game.R
import com.example.game.repository.GameRepository
import com.example.game.repository.WheelImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class Game1ViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    val gamerBalance: StateFlow<Int> = gameRepository.gamerBalance
    val gamerRate: StateFlow<Int> = gameRepository.gamerRateGame1
    val listWheel1: Flow<List<WheelImages>> = gameRepository.listWheel1
    val listWheel2: Flow<List<WheelImages>> = gameRepository.listWheel2
    val listWheel3: Flow<List<WheelImages>> = gameRepository.listWheel3

    init {
        gameRepository.startRateGame1()
    }

    fun changeRate(plus: Boolean) {
        gameRepository.changeRateGame1(plus)
    }

    fun setGamerBalance(addSum: Int) {
        gameRepository.setGamerBalance(addSum)
    }

    fun changePosition(wheel: Int) {
        gameRepository.changePosition(wheel)
    }

}