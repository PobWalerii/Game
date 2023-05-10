package com.example.game.wheels

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.ui.adapter.WheelsAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
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
        game: Int
    ) {

        val listImages = if (game == 1) {
            listOf(
                R.drawable.game1_slot1,
                R.drawable.game1_slot2,
                R.drawable.game1_slot3,
                R.drawable.game1_slot4,
                R.drawable.game1_slot5_,
                R.drawable.game1_slot6,
                R.drawable.game1_slot7
            )
        } else
            listOf(
                R.drawable.game1_slot1,
                R.drawable.game1_slot2,
                R.drawable.game1_slot3,
                R.drawable.game1_slot4,
                R.drawable.game1_slot5_,
            )

        wheel1 = OneWheel(adapter1, recycler1, getRandomList(listImages), lifecycleOwner)
        wheel2 = OneWheel(adapter2, recycler2, getRandomList(listImages), lifecycleOwner)
        wheel3 = OneWheel(adapter3, recycler3, getRandomList(listImages), lifecycleOwner)

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
                        startVibrator()
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

    private fun getRandomList(listImages: List<Int>): MutableList<WheelImages> {
        val list = mutableListOf<WheelImages>()
        val random = Random()
        var startImage = random.nextInt(listImages.size)
        repeat(listImages.size*2) {
            list.add(WheelImages(startImage+1.toLong(), listImages[startImage]))
            startImage++
            if (startImage == listImages.size) {
                startImage = 0
            }
        }
        return list
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