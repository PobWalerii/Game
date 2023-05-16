package com.example.game.ui.game2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.game.databinding.FragmentGame2Binding
import com.example.game.gamesclasses.rows.RowsManager
import com.example.game.ui.main.MainActivity
import com.example.game.ui.viewmodel.GameViewModel
import com.example.game.utils.ScreenStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Game2Fragment : Fragment() {

    private var _binding: FragmentGame2Binding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<GameViewModel>()

    @Inject lateinit var rowsManager: RowsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGame2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRowManager()
        observeGamersBalance()
        observeRateChange()
        rateKeysListener()
        observeSplinPress()
        observePlayStatus()
        setupNaviButton()
    }

    private fun setupRowManager() {
        rowsManager.init(
            binding.containerInclude.row1,
            binding.containerInclude.row2,
            binding.containerInclude.row3,
            viewLifecycleOwner,
        )
    }

    private fun observeGamersBalance() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerBalance.collect { balance ->
                binding.balance2Include.balance2 = balance
            }
        }
    }

    private fun observeRateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerRate.collect { rate ->
                binding.rate2Include.rate2 = rate
            }
        }
    }

    private fun rateKeysListener() {
        binding.plus.oper.setOnClickListener {
            viewModel.changeRate(true)
        }
        binding.minus.oper.setOnClickListener {
            viewModel.changeRate(false)
        }
    }

    private fun observeSplinPress() {
        binding.splinInclude.splin.setOnClickListener {
            rowsManager.startPlay()
        }
    }

    private fun observePlayStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                viewModel.isRowsPlay,
                viewModel.gamerRate
            ) { isPlay, rate ->
                !isPlay && rate !=0
            }.collect {
                binding.splinInclude.isEnable = it
                ScreenStatus.setScreenStatus(requireActivity(), it)
            }
        }
    }

    private fun setupNaviButton() {
        binding.back.setOnClickListener {
            (activity as MainActivity).onSupportNavigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}