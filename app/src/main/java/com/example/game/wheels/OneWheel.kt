package com.example.game.wheels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.utils.RecyclerViewDisabler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

class OneWheel(
    private val adapter: WheelsAdapter,
    private val recycler: RecyclerView,
    private val numberWheel: Int,
    private val lifecycleOwner: LifecycleOwner,
) {

    private val _wheelChangePosition = MutableSharedFlow<Int>()
    val wheelChangePosition: SharedFlow<Int> = _wheelChangePosition.asSharedFlow()

    private val _isRotate = MutableStateFlow(false)
    val isRotate: StateFlow<Boolean> = _isRotate.asStateFlow()

    private val _isStop = MutableStateFlow(true)
    val isStop: StateFlow<Boolean> = _isStop.asStateFlow()

    private var job: Job? = null

    init {
        recycler.adapter = adapter
        recycler.itemAnimator = null
        recycler.addOnItemTouchListener(RecyclerViewDisabler())

        lifecycleOwner.lifecycleScope.launchWhenStarted {

            recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)

            val layoutManager = recycler.layoutManager as LinearLayoutManager
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition == adapter.itemCount - 2) {
                        changePosition()
                    }
                }
            })
        }
    }

    private fun changePosition() {
        CoroutineScope(Dispatchers.Main).launch {
            _wheelChangePosition.emit(numberWheel)
        }
    }

    fun startRotate(shiftSize: Int) {
        val random = Random()
        val start = if(numberWheel==1) {
            0
        } else {
            random.nextInt(750).toLong()
        }
        val speed = random.nextInt(3)

        _isRotate.value = true
        _isStop.value = false
        job = CoroutineScope(Dispatchers.Default).launch {
            delay(start)
            repeat(shiftSize / 4) { i ->
                withContext(Dispatchers.Main){recycler.smoothScrollBy(0, -i*4)}
                delay(20)
            }
            repeat(100) { i ->
                withContext(Dispatchers.Main){ recycler.smoothScrollBy(0, -speed*shiftSize) }
                delay(20)
            }
            withContext(Dispatchers.Main){recycler.smoothScrollBy(0, -shiftSize/4)}
            delay(20)
            withContext(Dispatchers.Main){
                recycler.smoothScrollBy(0, 2*shiftSize)
                stopRotate()
            }
        }
    }

    private fun stopRotate() {
        job?.cancel()
        recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)
        _isRotate.value = false
        _isStop.value = true
    }

}