package com.example.game.ui.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.databinding.FragmentGame1Binding
import com.example.game.ui.adapter.WheelsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Game1Fragment : Fragment() {

    private var _binding: FragmentGame1Binding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<Game1ViewModel>()
    private lateinit var adapter1: WheelsAdapter
    private lateinit var adapter2: WheelsAdapter
    private lateinit var adapter3: WheelsAdapter
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGame1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        observeWheelsState()
        scrollWheelsToBottom()
        observeGamersBalance()
        observeRateChange()
        rateKeysListener()
        clickAndRotate()
    }


    private fun observeGamersBalance() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerBalance.collect { balance ->
                binding.balanceInclude.balance = balance
            }
        }
    }

    private fun observeRateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerRate.collect { rate ->
                binding.rateInclude.rate = rate
                binding.splinInclude.isEnable = rate != 0
            }
        }
    }

    private fun rateKeysListener() {
        binding.plus.fieldClick.setOnClickListener {
            viewModel.changeRate(true)
        }
        binding.minus.fieldClick.setOnClickListener {
            viewModel.changeRate(false)
        }
    }

    private fun clickAndRotate() {
        binding.splinInclude.splin.setOnClickListener {
            startScrolling()
        }
    }

    private fun startScrolling() {
        val imageSize = resources.getDimensionPixelSize(R.dimen.size_image_slot)/2
        viewLifecycleOwner.lifecycleScope.launch {
            //for (i in 1..10) {
                recyclerView1.smoothScrollBy(0, -imageSize)
                recyclerView2.smoothScrollBy(0, -imageSize)
                recyclerView3.smoothScrollBy(0, -imageSize)
                //delay(1000)
            //}
        }





    }




    private fun observeWheelsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel1.collect {
                if (it.isNotEmpty()) {
                    adapter1.submitList(it.reversed())
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel2.collect {
                if (it.isNotEmpty()) {
                    adapter2.submitList(it.reversed())
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel3.collect {
                if (it.isNotEmpty()) {
                    adapter3.submitList(it.reversed())
                }
            }
        }
    }

    private fun setupAdapters() {
        adapter1 = WheelsAdapter()
        adapter2 = WheelsAdapter()
        adapter3 = WheelsAdapter()
    }

    private fun setupRecyclers() {
        recyclerView1 = binding.wheel1.recycler
        recyclerView1.adapter = adapter1
        setScrollListener(1)
        recyclerView2 = binding.wheel2.recycler
        recyclerView2.adapter = adapter2
        setScrollListener(2)
        recyclerView3 = binding.wheel3.recycler
        recyclerView3.adapter = adapter3
        setScrollListener(3)
    }

    private fun setScrollListener(wheel: Int) {
        val recycler = when(wheel) {
            1 -> recyclerView1
            2 -> recyclerView2
            else -> recyclerView3
        }
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    Toast.makeText(context,"$lastVisibleItemPosition",Toast.LENGTH_LONG).show()
                    if (lastVisibleItemPosition == adapter1.itemCount - 1) {
                        Toast.makeText(context,"change",Toast.LENGTH_LONG).show()
                        viewModel.changePosition(wheel)
                    }
                }
            }
        })
    }
    private fun scrollWheelsToBottom() {
        recyclerView1.layoutManager?.scrollToPosition(adapter1.itemCount-1)
        recyclerView2.layoutManager?.scrollToPosition(adapter2.itemCount-1)
        recyclerView3.layoutManager?.scrollToPosition(adapter3.itemCount-1)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}