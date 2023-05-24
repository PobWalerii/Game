package com.example.game.ui.basefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.game.BR
import com.example.game.R
import com.example.game.gameclasses.rows.RowsManager
import com.example.game.ui.main.MainActivity
import com.example.game.ui.viewmodel.GameViewModel
import com.example.game.utils.ScreenStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseGameFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = requireNotNull(_binding)

    private val balance2Binding: ViewDataBinding? by lazy {
        DataBindingUtil.bind(binding.root.findViewById(R.id.balance2_include))
    }
    private val rate2Binding: ViewDataBinding? by lazy {
        DataBindingUtil.bind(binding.root.findViewById(R.id.rate2_include))
    }
    private val splinBinding: ViewDataBinding? by lazy {
        DataBindingUtil.bind(binding.root.findViewById(R.id.splin_include))
    }

    private val _isPressPlus = MutableStateFlow(false)
    val isPressPlus: StateFlow<Boolean> = _isPressPlus.asStateFlow()

    private val _isPressMinus = MutableStateFlow(false)
    val isPressMinus: StateFlow<Boolean> = _isPressMinus.asStateFlow()

    abstract var gameNumber: Int

    protected lateinit var viewModel: GameViewModel
    protected abstract fun getViewModelClass(): Class<GameViewModel>

    @Inject
    lateinit var rowsManager: RowsManager

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRowManager()
        observeGamersBalance()
        observeRateChange()
        rateKeysListener()
        observeLongKeyPress()
        observeSplinPress()
        observePlayStatus()
        setupNaviButton()
    }

    private fun setupRowManager() {
        rowsManager.init(
            gameNumber,
            binding.root.findViewById(R.id.container_include),
            viewLifecycleOwner
        )
    }

    private fun observeGamersBalance() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerBalance.collect { balance ->
                balance2Binding?.setVariable(BR.balance2, balance)
            }
        }
    }

    private fun observeRateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerRate.collect { rate ->
                rate2Binding?.setVariable(BR.rate2,rate)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun rateKeysListener() {
        val buttonPlus = binding.root.findViewById<View>(R.id.plus)
        val buttonMinus = binding.root.findViewById<View>(R.id.minus)
        buttonPlus.setOnClickListener {
            viewModel.changeRate(true)
        }
        buttonMinus.setOnClickListener {
            viewModel.changeRate(false)
        }
        buttonPlus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    _isPressPlus.value = true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    _isPressPlus.value = false
                }
            }
            true
        }
        buttonMinus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    _isPressMinus.value = true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    _isPressMinus.value = false
                }
            }
            true
        }
    }

    private fun observeLongKeyPress() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine( isPressPlus, isPressMinus, viewModel.gamerRate) { isPlus, isMinus, rate ->
                Triple(isPlus, isMinus, rate)}.collect {(isPlus, isMinus) ->
                    if(isPlus || isMinus) {
                        viewModel.changeRate(isPlus)
                    }
            }
        }
    }

    private fun observeSplinPress() {
        binding.root.findViewById<View>(R.id.splin_include).setOnClickListener {
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
                splinBinding?.setVariable(BR.isEnable,it)
                ScreenStatus.setScreenStatus(requireActivity(), it)
            }
        }
    }

    private fun setupNaviButton() {
        binding.root.findViewById<View>(R.id.back).setOnClickListener {
            (activity as MainActivity).onSupportNavigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}