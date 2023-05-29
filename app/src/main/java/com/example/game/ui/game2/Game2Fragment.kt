package com.example.game.ui.game2

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.game.databinding.FragmentGame2Binding
import com.example.game.ui.basefragment.BaseGameFragment
import com.example.game.ui.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game2Fragment : BaseGameFragment<FragmentGame2Binding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGame2Binding {
        return FragmentGame2Binding.inflate(inflater, container, false)
    }
    override fun getViewModelClass() = GameViewModel::class.java

    override var gameNumber = 2

}
