package com.example.game.ui.game1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.game.databinding.FragmentGame1Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


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
        initWheels()
        observeGamersBalance()
        observeRateChange()
        rateKeysListener()
        clickAndRotete()
    }

    private fun initWheels() {
        for(i in 1..3) {
            val random = Random()
            val startImage = random.nextInt(viewModel.list_images.size) + 1






        }
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
            startScrolling()

            /*
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

             */



        }
    }

    private fun startScrolling() {

        /*
        val scrollSpeed = 10
        val imageWidth = 100 // ширина картинки
        val imageHeight = 100 // высота картинки
        val screenHeight = binding.root.height
        val imageViews = listOf(binding.imageView1, binding.imageView2, binding.imageView3)

        // Инициализация списка картинок
        imageViews.forEachIndexed { index, imageView ->
            imageView.setImageResource(R.drawable.image_${index + 1})
            imageView.y = index * imageHeight.toFloat()
        }

        // Запуск бесконечного цикла прокрутки
        while (true) {
            for (imageView in imageViews) {
                imageView.y += scrollSpeed
                if (imageView.y > screenHeight) {
                    val topImageView = imageViews.first()
                    topImageView.y = imageViews.last().y - imageHeight
                    Collections.rotate(imageViews, -1)
                }
            }
            Thread.sleep(20)
        }

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}