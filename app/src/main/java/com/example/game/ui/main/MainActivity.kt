package com.example.game.ui.main

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.game.R
import com.example.game.utils.AppSplashScreen.startSplash
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        startSplash(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideStatusBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainerView).navigateUp() || super.onSupportNavigateUp()
    }

    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = window.insetsController
            insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

}