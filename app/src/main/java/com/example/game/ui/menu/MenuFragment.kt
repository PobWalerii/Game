package com.example.game.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.game.databinding.FragmentMenuBinding
import com.example.game.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMenuListeners()
    }

    private fun setMenuListeners() {
        binding.play1.play.setOnClickListener {
            startGame1()
        }
        binding.play2.play.setOnClickListener {
            startGame2()
        }
        binding.play3.play.setOnClickListener {
            startGame3()
        }
        binding.arrow.setOnClickListener {
            (activity as MainActivity).finish()
        }
    }

    private fun startGame1() {
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToGame1Fragment())
    }

    private fun startGame2() {
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToGame2Fragment())
    }

    private fun startGame3() {
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToGame3Fragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}