package com.example.game.ui.game1

import androidx.lifecycle.ViewModel
import com.example.game.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class Game1ViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {
    val gamerBalance: StateFlow<Int> = gameRepository.gamerBalance

    fun setGamerBalance(addSumm: Int) {
        gameRepository.setGamerBalance(addSumm)
    }




}