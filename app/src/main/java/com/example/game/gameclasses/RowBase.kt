package com.example.game.gameclasses

import kotlinx.coroutines.flow.StateFlow

abstract class RowBase {
    abstract val isPlay: StateFlow<Boolean>
    abstract val isStop: StateFlow<Int>
    abstract fun stopAll(delay: Int)
    abstract fun finishAnim()
    abstract fun startPlay(order: Int, speed: Int)
}