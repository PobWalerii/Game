package com.example.game.ui.game3

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.game.databinding.FragmentGame3Binding
import com.example.game.ui.basefragment.BaseGameFragment
import com.example.game.ui.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game3Fragment : BaseGameFragment<FragmentGame3Binding>() {
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGame3Binding {
        return FragmentGame3Binding.inflate(inflater, container, false)
    }
    override fun getViewModelClass() = GameViewModel::class.java

    override var gameNumber = 1

}