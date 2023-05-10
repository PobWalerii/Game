package com.example.game.ui.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.game.databinding.FragmentGame1Binding
import com.example.game.ui.adapter.WheelsAdapter
import com.example.game.ui.main.MainActivity
import com.example.game.ui.viewmodel.GameViewModel
import com.example.game.utils.ScreenStatus.setScreenStatus
import com.example.game.wheels.WheelsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@AndroidEntryPoint
class Game1Fragment : Fragment() {

    private var _binding: FragmentGame1Binding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<GameViewModel>()
    private lateinit var adapter1: WheelsAdapter
    private lateinit var adapter2: WheelsAdapter
    private lateinit var adapter3: WheelsAdapter
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView

    @Inject lateinit var wheelsManager: WheelsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGame1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupRecyclers()
        setupWheelsManager()
        observeGamersBalance()
        observeRateChange()
        rateKeysListener()
        observeSplinPress()
        observeRotateStatus()
        setupNaviButton()
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

    private fun observeSplinPress() {
        binding.splinInclude.splin.setOnClickListener {
            wheelsManager.startRotate()
        }
    }

    private fun observeRotateStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                viewModel.isWheelsRotate,
                viewModel.gamerRate
            ) { isRotate, rate ->
                !isRotate && rate !=0
            }.collect {
                binding.splinInclude.isEnable = it
                setScreenStatus(requireActivity(), it)
            }
        }
    }

    private fun setupAdapters() {
        adapter1 = WheelsAdapter()
        adapter2 = WheelsAdapter()
        adapter3 = WheelsAdapter()
    }

    private fun setupRecyclers() {
        recyclerView1 = binding.layoutWheels.wheel1.recycler
        recyclerView2 = binding.layoutWheels.wheel2.recycler
        recyclerView3 = binding.layoutWheels.wheel3.recycler
    }

    private fun setupWheelsManager() {
        wheelsManager.init(
            adapter1,
            adapter2,
            adapter3,
            recyclerView1,
            recyclerView2,
            recyclerView3,
            viewLifecycleOwner,
        )
    }

    private fun setupNaviButton() {
        binding.arrow.setOnClickListener {
            (activity as MainActivity).onSupportNavigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}