package com.example.game.ui.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.databinding.FragmentGame1Binding
import com.example.game.ui.adapter.WheelsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


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
        clickAndRotete()
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

    private fun clickAndRotete() {
        binding.splinInclude.splin.setOnClickListener {
            startScrolling()

        }
    }

    private fun startScrolling() {
        val imageSize = resources.getDimensionPixelSize(R.dimen.size_image_slot)
        recyclerView1.smoothScrollBy(0, -imageSize)
        recyclerView2.smoothScrollBy(0, -imageSize)
        recyclerView3.smoothScrollBy(0, -imageSize)
    }


    private fun observeWheelsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel1.collect {
                if (it.isNotEmpty()) {
                    adapter1.setImagesList(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel2.collect {
                if (it.isNotEmpty()) {
                    adapter2.setImagesList(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listWheel3.collect {
                if (it.isNotEmpty()) {
                    adapter3.setImagesList(it)
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