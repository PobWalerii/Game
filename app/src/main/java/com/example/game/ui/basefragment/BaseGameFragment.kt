package com.example.game.ui.basefragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.game.BR
import com.example.game.R
import com.example.game.gameclasses.RowsManager
import com.example.game.ui.main.MainActivity
import com.example.game.ui.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseGameFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = requireNotNull(_binding)

    private val balanceBinding: ViewDataBinding? by lazy {
        DataBindingUtil.bind(binding.root.findViewById(R.id.balance_include))
    }
    private val rateBinding: ViewDataBinding? by lazy {
        DataBindingUtil.bind(binding.root.findViewById(R.id.rate_include))
    }
    private val splinBinding: ViewDataBinding? by lazy {
        DataBindingUtil.bind(binding.root.findViewById(R.id.splin_include))
    }

    private val _isPressPlus = MutableStateFlow(false)
    private val isPressPlus: StateFlow<Boolean> = _isPressPlus.asStateFlow()

    private val _isPressMinus = MutableStateFlow(false)
    private val isPressMinus: StateFlow<Boolean> = _isPressMinus.asStateFlow()

    abstract var gameNumber: Int

    private lateinit var viewModel: GameViewModel
    protected abstract fun getViewModelClass(): Class<GameViewModel>

    @Inject
    lateinit var rowsManager: RowsManager

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
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
        observeSplinPress()
        observePlayStatus()
        setupNaviButton()
    }

    private fun setupRowManager() {
        rowsManager.init(
            gameNumber,
            binding.root.findViewById(R.id.container_include),
            viewLifecycleOwner,
        )
    }

    private fun observeGamersBalance() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerBalance.collect { balance ->
                balanceBinding?.setVariable(BR.balance, balance)
            }
        }
    }

    private fun observeRateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamerRate.collect { rate ->
                rateBinding?.setVariable(BR.rate,rate)
                if(isPressPlus.value || isPressMinus.value) {
                    delay(1)
                    viewModel.changeRate(isPressPlus.value)
                }
            }
        }
    }

    private fun rateKeysListener() {
        val buttonPlus = binding.root.findViewById<View>(R.id.plus)
        val buttonMinus = binding.root.findViewById<View>(R.id.minus)

        buttonPlus.setOnClickListener {
            viewModel.changeRate(true)
        }
        buttonMinus.setOnClickListener {
            viewModel.changeRate(false)
        }
        buttonPlus.setOnLongClickListener {
            _isPressPlus.value = true
            createTouch(true)
            viewModel.changeRate(true)
            true
        }
        buttonMinus.setOnLongClickListener {
            _isPressMinus.value = true
            createTouch(false)
            viewModel.changeRate(false)
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createTouch(oper: Boolean) {
        val button = if(oper) {
            binding.root.findViewById(R.id.plus)
        } else {
            binding.root.findViewById<View>(R.id.minus)
        }
        val touchListener = OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    clearTouch()
                }
            }
            true
        }
        button.setOnTouchListener(touchListener)
    }

    private fun clearTouch() {
        _isPressPlus.value = false
        _isPressMinus.value = false
        val buttonPlus = binding.root.findViewById<View>(R.id.plus)
        val buttonMinus = binding.root.findViewById<View>(R.id.minus)
        buttonPlus.setOnTouchListener(null)
        buttonMinus.setOnTouchListener(null)
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
                setScreenStatus(it)
            }
        }
    }

    private fun setScreenStatus(unblock: Boolean) {
        activity?.requestedOrientation =
            if (unblock) {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            } else {
                ActivityInfo.SCREEN_ORIENTATION_LOCKED
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