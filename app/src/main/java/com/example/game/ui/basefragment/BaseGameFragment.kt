package com.example.game.ui.basefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.game.R
import com.example.game.gamesclasses.rows.RowsManager
import com.example.game.ui.main.MainActivity
import com.example.game.ui.viewmodel.GameViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseGameFragment<T : ViewBinding>(
    private val inflateMethod : (LayoutInflater, ViewGroup?, Boolean) -> T
) : Fragment() {

    private var _binding: T? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<GameViewModel>()

    @Inject
    lateinit var rowsManager: RowsManager

    open fun T.initialize(){
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(inflater, container, false)
        binding.initialize()
        return _binding!!.root
=======
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.game.R
import com.example.game.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseGameFragment : Fragment() {

    protected abstract val layoutResId: Int
    private var _binding: ViewDataBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
>>>>>>> origin/main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
<<<<<<< HEAD
        observeRateChange()
        rateKeysListener()
        setupNaviButton()
    }

    private fun observeRateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerRate.collect { rate ->
                //childFragmentManager.setRate(rate)
            }
        }
    }

    private fun rateKeysListener() {
        //binding.plus.oper.setOnClickListener {
        //    viewModel.changeRate(true)
        //}
        //binding.minus.oper.setOnClickListener {
        //    viewModel.changeRate(false)
        //}
    }


    private fun setupNaviButton() {
        binding.root.findViewById<ImageView>(R.id.back).setOnClickListener {
=======
        binding.root.findViewById<View>(R.id.back).setOnClickListener {
>>>>>>> origin/main
            (activity as MainActivity).onSupportNavigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
