package com.example.game.ui.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.game.R
import com.example.game.databinding.FragmentGame1Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Game1Fragment : Fragment() {

    private var _binding: FragmentGame1Binding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<Game1ViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGame1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            Toast.makeText(context,"Rotate",Toast.LENGTH_SHORT).show()

            val wheelContainer: FrameLayout = binding.wheel1.wheelContainer
            val rotateAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
            for (i in 0 until wheelContainer.childCount) {
                val wheel: ImageView = wheelContainer.getChildAt(i) as ImageView
                wheel.startAnimation(rotateAnimation)
            }

            for (i in 0 until wheelContainer.childCount) {
                val wheel = wheelContainer.getChildAt(i) as ImageView
                wheel.clearAnimation()
            }



        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}