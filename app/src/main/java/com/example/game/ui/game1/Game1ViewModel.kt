package com.example.game.ui.game1

import androidx.lifecycle.ViewModel
import com.example.game.R
import com.example.game.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class Game1ViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    val gamerBalance: StateFlow<Int> = gameRepository.gamerBalance
    val gamerRate: StateFlow<Int> = gameRepository.gamerRateGame1
    val list_images: List<Int> = listOf(
        R.drawable.game1_slot1,
        R.drawable.game1_slot2,
        R.drawable.game1_slot3,
        R.drawable.game1_slot4,
        R.drawable.game1_slot5,
        R.drawable.game1_slot6,
        R.drawable.game1_slot7,
    )

    init {
        gameRepository.startRateGame1()
    }

    fun changeRate(plus: Boolean) {
        gameRepository.changeRateGame1(plus)
    }

    fun setGamerBalance(addSumm: Int) {
        gameRepository.setGamerBalance(addSumm)
    }

}