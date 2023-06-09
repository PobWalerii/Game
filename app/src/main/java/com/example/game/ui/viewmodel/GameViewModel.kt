package com.example.game.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.game.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    val gamerBalance: StateFlow<Int> = gameRepository.gamerBalance
    val gamerRate: StateFlow<Int> = gameRepository.rateGame
    val isRowsPlay: StateFlow<Boolean> = gameRepository.isRowsPlay
    fun changeRate(plus: Boolean) {
        gameRepository.changeRateGame(plus)
    }

}