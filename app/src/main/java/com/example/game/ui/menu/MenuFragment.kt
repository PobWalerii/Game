package com.example.game.ui.menu

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        binding.play1.setOnClickListener {
            startGame1()
        }
        binding.play2.setOnClickListener {
            startGame2()
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        _binding = null
    }



}