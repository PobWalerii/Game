package com.example.game.wheels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.ui.adapter.WheelsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WheelsManager @Inject constructor(
    private val context: Context
) {
    private val _gameNumber = MutableStateFlow(0)
    val gameNumber: StateFlow<Int> = _gameNumber.asStateFlow()

    private val _isRotate = MutableStateFlow(false)
    val isRotate: StateFlow<Boolean> = _isRotate.asStateFlow()

    private val _isStop = MutableStateFlow(false)
    val isStop: StateFlow<Boolean> = _isStop.asStateFlow()

    lateinit var wheelChangePosition1: SharedFlow<Int>
    lateinit var wheelChangePosition2: SharedFlow<Int>
    lateinit var wheelChangePosition3: SharedFlow<Int>
    private lateinit var wheel1: OneWheel
    private lateinit var wheel2: OneWheel
    private lateinit var wheel3: OneWheel

    private var firstRotate = false
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun init(adapter1: WheelsAdapter,
             adapter2: WheelsAdapter,
             adapter3: WheelsAdapter,
             recycler1: RecyclerView,
             recycler2: RecyclerView,
             recycler3: RecyclerView,
             game: Int,
             lifecycleOwner: LifecycleOwner,
    ) {
        wheel1 = OneWheel(adapter1, recycler1, 1,lifecycleOwner)
        wheel2 = OneWheel(adapter2, recycler2, 2,lifecycleOwner)
        wheel3 = OneWheel(adapter3, recycler3, 3,lifecycleOwner)

        wheelChangePosition1 = wheel1.wheelChangePosition
        wheelChangePosition2 = wheel2.wheelChangePosition
        wheelChangePosition3 = wheel3.wheelChangePosition

        if (game != gameNumber.value) {
            _gameNumber.value = game
            coroutineScope.launch {
                combine(wheel1.isRotate, wheel2.isRotate, wheel3.isRotate) { isRt1, isRt2, isRt3 ->
                    isRt1 || isRt2 || isRt3
                }.collect {
                    if (it) {
                        _isRotate.value = true
                        firstRotate = true
                    }
                }
            }
            coroutineScope.launch {
                combine(wheel1.isStop, wheel2.isStop, wheel3.isStop) { isSt1, isSt2, isSt3 ->
                    Triple(isSt1, isSt2, isSt3)
                }.collect { (isSt1, isSt2, isSt3) ->
                    if (isSt1 || isSt2 || isSt3) {
                        if (firstRotate) {
                            startVibrator()
                        }
                    }
                    if(isSt1 && isSt2 && isSt3) {
                        _isStop.value = true
                    }
                }
            }
        }
    }

    fun fixsedStopRotate() {
        _isRotate.value = false
        _isStop.value = false
    }

    fun startRotate() {
        val shiftSize = context.resources.getDimensionPixelSize(R.dimen.size_image_slot)
        wheel1.startRotate(shiftSize)
        wheel2.startRotate(shiftSize)
        wheel3.startRotate(shiftSize)
    }

    @SuppressLint("ServiceCast")
    private fun startVibrator() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? Vibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(50)
        }
    }


}