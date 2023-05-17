package com.example.game.gameclasses.wheels

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.utils.RandomList.makeRandomList
import com.example.game.utils.Vibrator.startVibrator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WheelsManager @Inject constructor(
    private val context: Context
) {

    private val _isRotate = MutableStateFlow(false)
    val isRotate: StateFlow<Boolean> = _isRotate.asStateFlow()

    private val _gameResult = MutableStateFlow("")
    val gameResult: StateFlow<String> = _gameResult.asStateFlow()

    private lateinit var wheel1: OneWheel
    private lateinit var wheel2: OneWheel
    private lateinit var wheel3: OneWheel

    private var firstRotate = false

    fun init(
        adapter1: WheelsAdapter,
        adapter2: WheelsAdapter,
        adapter3: WheelsAdapter,
        recycler1: RecyclerView,
        recycler2: RecyclerView,
        recycler3: RecyclerView,
        lifecycleOwner: LifecycleOwner,
    ) {

        val listImages = listOf(
                R.drawable.game1_slot1,
                R.drawable.game1_slot2,
                R.drawable.game1_slot3,
                R.drawable.game1_slot4,
                R.drawable.game1_slot5,
                R.drawable.game1_slot6,
                R.drawable.game1_slot7
            )

        wheel1 = OneWheel(adapter1, recycler1, makeRandomList(listImages,2), lifecycleOwner)
        wheel2 = OneWheel(adapter2, recycler2, makeRandomList(listImages,2), lifecycleOwner)
        wheel3 = OneWheel(adapter3, recycler3, makeRandomList(listImages,2), lifecycleOwner)

        lifecycleOwner.lifecycleScope.launch {
            combine(wheel1.isRotate, wheel2.isRotate, wheel3.isRotate) { isRt1, isRt2, isRt3 ->
                isRt1 || isRt2 || isRt3
            }.collect {
                _isRotate.value = it
                if (it) {
                    firstRotate = true
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch {
            combine(wheel1.isStop, wheel2.isStop, wheel3.isStop) { isSt1, isSt2, isSt3 ->
                Triple(isSt1, isSt2, isSt3)
            }.collect { (isSt1, isSt2, isSt3) ->
                if (isSt1 !=0 || isSt2 !=0 || isSt3 !=0) {
                    if (firstRotate) {
                        startVibrator(context)
                    }
                }
                if (isSt1 !=0 && isSt2 !=0 && isSt3 !=0) {
                    _gameResult.value = when {
                        isSt1 == isSt2 && isSt2 == isSt3 -> "x5"
                        isSt1 == isSt2 || isSt1 == isSt3 || isSt2 == isSt3 -> "x2"
                        else -> "--"
                    }
                    _isRotate.value = false
                }
            }
        }
    }

    fun startRotate() {
        _gameResult.value = ""
        val shiftSize =
            context.resources.getDimensionPixelSize(
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    R.dimen.size_image_slot
                } else {
                    R.dimen.size_image_slot_land
                }
            )
        val startOrder = mutableListOf(0,1,2)
        startOrder.shuffle()
        wheel1.startRotate(startOrder[0],shiftSize)
        wheel2.startRotate(startOrder[1],shiftSize)
        wheel3.startRotate(startOrder[2],shiftSize)
    }



}