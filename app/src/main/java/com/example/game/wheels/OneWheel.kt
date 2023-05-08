package com.example.game.wheels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.utils.RecyclerViewDisabler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OneWheel(
    private val adapter: WheelsAdapter,
    private val recycler: RecyclerView,
    private val numberWheel: Int,
    private val lifecycleOwner: LifecycleOwner,
) {

    private val _wheelChangePosition = MutableStateFlow(0)
    val wheelChangePosition: StateFlow<Int> = _wheelChangePosition.asStateFlow()

    private val _isRotate = MutableStateFlow(false)
    val isRotate: StateFlow<Boolean> = _isRotate.asStateFlow()

    private val _isStop = MutableStateFlow(true)
    val isStop: StateFlow<Boolean> = _isStop.asStateFlow()

    private var job: Job? = null

    init {
        recycler.adapter = adapter
        recycler.itemAnimator = null
        recycler.addOnItemTouchListener(RecyclerViewDisabler())

        CoroutineScope(Dispatchers.Main).launch {
            recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)
        }

        val layoutManager = recycler.layoutManager as LinearLayoutManager
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                _wheelChangePosition.value = 0
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == adapter.itemCount - 2) {
                    changePosition()
                }
            }
        })
    }

    private fun changePosition() {
        _wheelChangePosition.value = numberWheel
    }

    fun startRotate(shiftSize: Int, start: Long, speed: Int) {
        _isRotate.value = true
        _isStop.value = false
        job = lifecycleOwner.lifecycleScope.launch {
            delay(start)
            for(i in 1..shiftSize/4) {
                withContext(Dispatchers.Main){recycler.smoothScrollBy(0, -i*4)}
                delay(20)
            }
            for (i in 1..50) {
                withContext(Dispatchers.Main){recycler.smoothScrollBy(0, -speed*shiftSize)}
                delay(50)
            }
            withContext(Dispatchers.Main){recycler.smoothScrollBy(0, -shiftSize/4)}
            delay(20)
            withContext(Dispatchers.Main){recycler.smoothScrollBy(0, 2*shiftSize)}
            withContext(Dispatchers.Main){ stopRotate()}
        }


    }

    private fun stopRotate() {
        job?.cancel()
        _isRotate.value = false
        _isStop.value = true
    }

}