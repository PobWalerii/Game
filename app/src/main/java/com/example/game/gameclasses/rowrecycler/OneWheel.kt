package com.example.game.gameclasses.rowrecycler

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.constants.GamesConstants
import com.example.game.data.ItemImages
import com.example.game.gameclasses.utils.RowBase
import com.example.game.gameclasses.utils.ItemAnim.makeItemAnim
import com.example.game.gameclasses.utils.RecyclerViewDisabler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class OneWheel(
    private val recycler: RecyclerView,
    private val listImages: MutableList<ItemImages>,
    private val setDirection: Boolean?,
    private val lifecycleOwner: LifecycleOwner,
): RowBase() {

    private val _isPlay = MutableStateFlow(false)
    override val isPlay: StateFlow<Boolean> = _isPlay.asStateFlow()

    private val _isStop = MutableStateFlow(0)
    override val isStop: StateFlow<Int> = _isStop.asStateFlow()

    val adapter = WheelAdapter()

    private var direction = false

    private var stopPlay = false

    init {
        recycler.adapter = adapter
        recycler.itemAnimator = null
        recycler.addOnItemTouchListener(RecyclerViewDisabler())
        adapter.submitList(listImages.toList())
        recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (direction) {
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    if (firstPosition > 1) {
                        changePosition(firstPosition)
                    }
                } else {
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition < adapter.itemCount - 2) {
                        changePosition(adapter.itemCount - 2 - lastPosition)
                    }
                }
            }
        })
    }

    private fun changePosition(count: Int) {
        repeat(count) {
            if (direction) {
                val first = listImages[0]
                listImages.removeAt(0)
                listImages.add(first)
            } else {
                val last = listImages[listImages.size - 1]
                listImages.removeAt(listImages.size - 1)
                listImages.add(0, last)
            }
        }
        adapter.submitList(listImages.toList())
    }

    override fun startPlay(order: Int, speed: Int) {

        val dir = mutableListOf(false,true,false,true,false,true)
        dir.shuffle()
        direction = setDirection ?: dir[0]

        val layoutManager = recycler.layoutManager as LinearLayoutManager

        if(direction) {
            val firstPosition = layoutManager.findFirstVisibleItemPosition()
            if(firstPosition != 0) {
                changePosition(firstPosition)
            }
        } else {
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            if(lastPosition != adapter.itemCount - 1) {
                changePosition(adapter.itemCount - 1 - lastPosition)
            }
        }
        val childView = layoutManager.getChildAt(0)
        var shift = childView?.height ?: 0

        if (!direction) {
            shift = -shift
        }

        _isStop.value = 0
        _isPlay.value = true
        stopPlay = false

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(GamesConstants.DELAY_START_ROW_INTERVAL * order.toLong())

            repeat(10) {
                recycler.smoothScrollBy(0, shift/2)
                delay(20)
            }
            delay(20)
            for (i in 1..25) {
                recycler.smoothScrollBy(0, shift * speed * 3)
                delay(20 * speed.toLong())
                if (stopPlay) {
                    break
                }
            }
            recycler.smoothScrollBy(0, shift / 4)
            delay(20)
            recycler.smoothScrollBy(0, -shift * 2)
            stopPlay()
        }
    }

    override fun stopAll(delay: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(GamesConstants.DELAY_START_ROW_INTERVAL * delay)
            stopPlay = true
        }
    }

    private fun stopPlay() {
        _isPlay.value = false
        _isStop.value = listImages[if(direction) 1 else listImages.size-2].id.toInt()
    }

    override fun finishAnim() {
        val position = if(direction) 1 else listImages.size-2
        val view = recycler.findViewHolderForAdapterPosition(position)?.itemView
        makeItemAnim(view)
    }

}