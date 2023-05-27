package com.example.game.gameclasses.rows

import kotlinx.coroutines.flow.StateFlow

abstract class BaseRow {
    abstract val isPlay: StateFlow<Boolean>
    abstract val isStop: StateFlow<Int>
    abstract fun stopAll(delay: Int)
    abstract fun finishAnim()
    abstract fun startPlay(order: Int, speed: Int)
}