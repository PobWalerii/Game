package com.example.game.gamesclasses.wheels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.constants.GamesConstants.DELAY_START_WHEEL_INTERVAL
import com.example.game.dataclass.ItemImages
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.utils.RecyclerViewDisabler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class OneWheel(
    private val adapter: WheelsAdapter,
    private val recycler: RecyclerView,
    private val list: MutableList<ItemImages>,
    private val lifecycleOwner: LifecycleOwner,
) {

    private val _isRotate = MutableStateFlow(false)
    val isRotate: StateFlow<Boolean> = _isRotate.asStateFlow()

    private val _isStop = MutableStateFlow(0)
    val isStop: StateFlow<Int> = _isStop.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            recycler.adapter = adapter
            recycler.itemAnimator = null
            recycler.addOnItemTouchListener(RecyclerViewDisabler())
            adapter.submitList(list.reversed())
            recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)
        }
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition < adapter.itemCount - 2) {
                        changePosition(adapter.itemCount - 2 - lastVisibleItemPosition)
                    }
                }
            })
        }
    }

    private fun changePosition(count: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(count) {
                val first = list[0]
                list.removeAt(0)
                list.add(first)
                adapter.submitList(list.reversed())
            }
        }
    }

    fun startRotate(order: Int, shiftSize: Int) {
        val random = mutableListOf(1,2,3)
        random.shuffle()
        val speed = random[0]
        _isRotate.value = true
        _isStop.value = 0
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(DELAY_START_WHEEL_INTERVAL*order.toLong())

            repeat(shiftSize / 4) { i ->
                withContext(Dispatchers.Main){
                    recycler.smoothScrollBy(0, -i*10)
                    delay(20)
                }
            }

            for(i in 1..50) {
                withContext(Dispatchers.Main){
                    recycler.smoothScrollBy(0, -(2*shiftSize))
                }
                delay(20*speed.toLong())
            }

            withContext(Dispatchers.Main){
                recycler.smoothScrollBy(0, -shiftSize/4)
            }
            delay(20)
            withContext(Dispatchers.Main){
                recycler.smoothScrollBy(0, 2*shiftSize)
                stopRotate()
            }
        }
    }

    private fun stopRotate() {
        _isRotate.value = false
        _isStop.value = list[1].id.toInt()
    }

}