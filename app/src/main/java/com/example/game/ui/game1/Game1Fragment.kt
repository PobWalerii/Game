package com.example.game.ui.game1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.databinding.FragmentGame1Binding
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


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
        setScrollListeners()
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
            val orientation = resources.configuration.orientation
            val currentOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                SCREEN_ORIENTATION_PORTRAIT
            } else {
                SCREEN_ORIENTATION_LANDSCAPE
            }
            (activity as MainActivity).setRequestedOrientation(currentOrientation)
            startScrolling()
        }
    }

    private fun startScrolling() {
        binding.splinInclude.isEnable = false
        val shiftSize = resources.getDimensionPixelSize(R.dimen.size_image_slot)
        viewLifecycleOwner.lifecycleScope.launch {
            for(i in 1..shiftSize/4) {
                recyclerView1.smoothScrollBy(0, -i*4)
                delay(20)
            }
            for (i in 1..100) {
                recyclerView1.smoothScrollBy(0, -2*shiftSize)
                delay(20)
            }
            recyclerView1.smoothScrollBy(0, -shiftSize/4)
            delay(20)
            recyclerView1.smoothScrollBy(0, 2*shiftSize)
            startVibrator()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            for(i in 1..shiftSize/4) {
                recyclerView2.smoothScrollBy(0, -i*4)
                delay(20)
            }
            for (i in 1..100) {
                recyclerView2.smoothScrollBy(0, -2*shiftSize)
                delay(20)
            }
            recyclerView2.smoothScrollBy(0, -shiftSize/4)
            delay(20)
            recyclerView2.smoothScrollBy(0, 2*shiftSize)
            startVibrator()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(750)
            for(i in 1..shiftSize/4) {
                recyclerView3.smoothScrollBy(0, -i*4)
                delay(20)
            }
            for (i in 1..100) {
                recyclerView3.smoothScrollBy(0, -2*shiftSize)
                delay(20)
            }
            recyclerView3.smoothScrollBy(0, -shiftSize/4)
            delay(20)
            recyclerView3.smoothScrollBy(0, 2*shiftSize)
            startVibrator()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            binding.splinInclude.isEnable = true
        }

    }

    @SuppressLint("ServiceCast")
    private fun startVibrator() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? Vibrator
        } else {
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(50)
        }
    }

    private fun observeWheelsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel1.collect {
                if (it.isNotEmpty()) {
                    adapter1.submitList(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel2.collect {
                if (it.isNotEmpty()) {
                    adapter2.submitList(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel3.collect {
                if (it.isNotEmpty()) {
                    adapter3.submitList(it)
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
        recyclerView2 = binding.wheel2.recycler
        recyclerView2.adapter = adapter2
        recyclerView3 = binding.wheel3.recycler
        recyclerView3.adapter = adapter3
    }

    private fun setScrollListeners() {
        val layoutManager1 = recyclerView1.layoutManager as LinearLayoutManager
        recyclerView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItemPosition = layoutManager1.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition == adapter1.itemCount - 2) {
                        viewModel.changePosition(1)
                    }
            }
        })

        val layoutManager2 = recyclerView2.layoutManager as LinearLayoutManager
        recyclerView2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItemPosition = layoutManager2.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition == adapter2.itemCount - 2) {
                        viewModel.changePosition(2)
                    }
            }
        })

        val layoutManager3 = recyclerView3.layoutManager as LinearLayoutManager
        recyclerView3.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItemPosition = layoutManager3.findLastVisibleItemPosition()
                    if (lastVisibleItemPosition == adapter3.itemCount - 2) {
                        viewModel.changePosition(3)
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