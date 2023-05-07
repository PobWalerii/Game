package com.example.game.wheels

import androidx.recyclerview.widget.RecyclerView
import com.example.game.ui.adapter.WheelsAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Singleton

@Singleton
class WheelsManager() {

    private val _gameNumber = MutableStateFlow(0)
    val gameNumber: StateFlow<Int> = _gameNumber.asStateFlow()
    lateinit var wheelChangePosition1: StateFlow<Int>
    lateinit var wheelChangePosition2: StateFlow<Int>
    lateinit var wheelChangePosition3: StateFlow<Int>

    fun init(adapter1: WheelsAdapter,
             adapter2: WheelsAdapter,
             adapter3: WheelsAdapter,
             recycler1: RecyclerView,
             recycler2: RecyclerView,
             recycler3: RecyclerView,
             game: Int
    ) {
        val wheel1 = OneWheel(adapter1, recycler1, 1)
        val wheel2 = OneWheel(adapter2, recycler2, 2)
        val wheel3 = OneWheel(adapter3, recycler3, 3)
        wheelChangePosition1 = wheel1.wheelChangePosition
        wheelChangePosition2 = wheel2.wheelChangePosition
        wheelChangePosition3 = wheel3.wheelChangePosition
        _gameNumber.value = game
    }

}