package com.example.game.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.game.repository.GameRepository
import com.example.game.repository.WheelImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    val gamerBalance: StateFlow<Int> = gameRepository.gamerBalance
    val gamerRate: StateFlow<Int> = gameRepository.gamerRateGame1
    val listWheel1Game1: Flow<List<WheelImages>> = gameRepository.listWheel1Game1
    val listWheel2Game1: Flow<List<WheelImages>> = gameRepository.listWheel2Game1
    val listWheel3Game1: Flow<List<WheelImages>> = gameRepository.listWheel3Game1
    val listWheel1Game2: Flow<List<WheelImages>> = gameRepository.listWheel1Game2
    val listWheel2Game2: Flow<List<WheelImages>> = gameRepository.listWheel2Game2
    val listWheel3Game2: Flow<List<WheelImages>> = gameRepository.listWheel3Game2
    val isWheelsRotate: StateFlow<Boolean> = gameRepository.isWheelsRotate
    val isWheelsStoped: StateFlow<Boolean> = gameRepository.isWheelsStoped

    fun changeRate(game: Int, plus: Boolean) {
        gameRepository.changeRateGame(game, plus)
    }

}