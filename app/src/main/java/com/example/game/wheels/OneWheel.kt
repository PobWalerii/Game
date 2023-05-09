package com.example.game.wheels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.repository.WheelImages
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.utils.RecyclerViewDisabler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

class OneWheel(
    private val adapter: WheelsAdapter,
    private val recycler: RecyclerView,
    private val list: MutableList<WheelImages>,
    private val lifecycleOwner: LifecycleOwner,
) {

    //private val _wheelChangePosition = MutableSharedFlow<Int>()
    //val wheelChangePosition: SharedFlow<Int> = _wheelChangePosition.asSharedFlow()

    private val _isRotate = MutableStateFlow(false)
    val isRotate: StateFlow<Boolean> = _isRotate.asStateFlow()

    private val _isStop = MutableStateFlow(true)
    val isStop: StateFlow<Boolean> = _isStop.asStateFlow()

    //private val _listWheel = MutableStateFlow(emptyList<WheelImages>())
    //val listWheel: Flow<List<WheelImages>> = _listWheel.asStateFlow()

    //private var job: Job? = null

    init {
        recycler.adapter = adapter
        recycler.itemAnimator = null
        recycler.addOnItemTouchListener(RecyclerViewDisabler())
        adapter.submitList(list.reversed())
        recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)


        /*

        lifecycleOwner.lifecycleScope.launchWhenStarted {


            val layoutManager = recycler.layoutManager as LinearLayoutManager
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition == 3 ){  //adapter.itemCount - 1) {
                        changePosition()
                    }
                }
            })
        }
         */

    }

    //private fun changePosition() {
    //    CoroutineScope(Dispatchers.Main).launch {
    //        _wheelChangePosition.emit(numberWheel)
    //    }
    //}



    fun startRotate(shiftSize: Int) {
        val random = Random()
        val start = random.nextInt(750).toLong()
        val speed = shiftSize*random.nextInt(3)

        _isRotate.value = true
        _isStop.value = false
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(start)
            //repeat(shiftSize / 4) { i ->
            //    withContext(Dispatchers.Main){recycler.smoothScrollBy(0, -i*4)}
            //    delay(20)
            //}
            repeat(100) { i ->
                withContext(Dispatchers.Main){
                    recycler.smoothScrollBy(0, -(speed))
                }
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
        //recycler.layoutManager?.scrollToPosition(adapter.itemCount - 1)
        _isRotate.value = false
        _isStop.value = true
    }

}