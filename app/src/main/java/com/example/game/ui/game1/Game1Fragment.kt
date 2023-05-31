package com.example.game.ui.game1

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.game.databinding.FragmentGame1Binding
import com.example.game.ui.viewmodel.GameViewModel
import com.example.game.ui.basefragment.BaseGameFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Game1Fragment : BaseGameFragment<FragmentGame1Binding>() {
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGame1Binding {
        return FragmentGame1Binding.inflate(inflater, container, false)
    }
    override fun getViewModelClass() = GameViewModel::class.java

    override var gameNumber = 1

}
