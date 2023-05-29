package com.example.game.gameclasses.rowrecycler

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.constants.GamesConstants
import com.example.game.data.ItemImages
import com.example.game.gameclasses.RowBase
import com.example.game.utils.RecyclerViewDisabler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class OneWheel(
    private val rowBinding: RecyclerView,
    private val listImages: MutableList<ItemImages>,
    private val setDirection: Boolean?,
    private val slide: Boolean,
    private val lifecycleOwner: LifecycleOwner,
): RowBase() {

    private val _isPlay = MutableStateFlow(false)
    override val isPlay: StateFlow<Boolean> = _isPlay.asStateFlow()

    private val _isStop = MutableStateFlow(0)
    override val isStop: StateFlow<Int> = _isStop.asStateFlow()

    val adapter = WheelAdapter()
    val recycler = rowBinding
    private lateinit var layoutManager: LinearLayoutManager

    private var direction = false

    private var stopPlay = false

    init {
            recycler.adapter = adapter
            recycler.itemAnimator = null
            recycler.addOnItemTouchListener(RecyclerViewDisabler())
            adapter.submitList(listImages.toList())
            recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)
            layoutManager = recycler.layoutManager as LinearLayoutManager
        //lifecycleOwner.lifecycleScope.launchWhenStarted {
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition < adapter.itemCount - 2) {
                        changePosition(adapter.itemCount - 2 - lastPosition)
                    }
                }
            })
        //}
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
                adapter.submitList(listImages.toList())
            }
    }


    override fun stopAll(delay: Int) {
    }
    override fun finishAnim() {
    }

    override fun startPlay(order: Int, speed: Int) {
        //val dir = mutableListOf(false,true,false,true,false,true)
        //dir.shuffle()
        //direction = setDirection ?: dir[0]

        val childView = layoutManager.getChildAt(0)
        var shift = childView?.height ?: 0
        //if (direction) {
        //    shift = -shift
        //}

        _isStop.value = 0
        _isPlay.value = true
        stopPlay = false

        lifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(GamesConstants.DELAY_START_ROW_INTERVAL * order.toLong())

            repeat(shift / 4) { i ->
                //withContext(Dispatchers.Main){
                    recycler.smoothScrollBy(0, -i*10)
                    delay(20)
                //}
            }

            for(i in 1..50) {
                //withContext(Dispatchers.Main){
                    recycler.smoothScrollBy(0, -shift*speed*3)
                //}
                delay(20*speed.toLong())
            }

            //withContext(Dispatchers.Main){
                recycler.smoothScrollBy(0, -shift/4)
            //}
            delay(10)
            //withContext(Dispatchers.Main){
                recycler.smoothScrollBy(0, 2*shift)
                stopPlay()
            //}
        }

    }


    private fun stopPlay() {
        _isPlay.value = false
        _isStop.value = listImages[listImages.size-2].id.toInt()
    }

}