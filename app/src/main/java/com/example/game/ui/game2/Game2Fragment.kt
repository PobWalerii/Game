package com.example.game.ui.game2

import com.example.game.R
import com.example.game.ui.basefragment.BaseGameFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class GameFragment1 : BaseGameFragment() {

    override val layoutResId = R.layout.fragment_game2

}
/*
class Game2Fragment : Fragment() {

    private val args: Game2FragmentArgs by navArgs()

    private var _binding: Any? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModels<GameViewModel>()

    @Inject lateinit var rowsManager: RowsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
         when(args.gameNumber) {
            1 -> {
                _binding = FragmentGame1Binding.inflate(inflater, container, false)
                return (binding as FragmentGame1Binding).root
            }
            else -> {
                _binding = FragmentGameBinding.inflate(inflater, container, false)
                return (binding as FragmentGameBinding).root
            }
        }

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

 */