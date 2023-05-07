package com.example.game.wheels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.utils.RecyclerViewDisabler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable.children
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
class OneWheel(
    adapter: WheelsAdapter,
    recycler: RecyclerView,
    numberWheel: Int
) {

    private val _wheelChangePosition = MutableStateFlow(0)
    val wheelChangePosition: StateFlow<Int> = _wheelChangePosition.asStateFlow()

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
                    _wheelChangePosition.value = numberWheel

                    //viewModel.changePosition(1,1)

                }
            }
        })
    }




}