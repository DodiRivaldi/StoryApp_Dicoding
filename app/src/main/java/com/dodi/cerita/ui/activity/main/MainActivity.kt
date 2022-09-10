package com.dodi.cerita.ui.activity.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.Constant.TIME_SPLASH
import com.dodi.cerita.databinding.ActivityMainBinding
import com.dodi.cerita.ui.activity.home.HomeActivity
import com.dodi.cerita.ui.activity.home.HomeActivity.Companion.TOKEN
import com.dodi.cerita.ui.activity.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.viewModelScope.launch {
                viewModel.getToken().collect {
                    if (it.isNullOrEmpty()) {
                        Intent(this@MainActivity, UserActivity::class.java).also { intent ->
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Intent(this@MainActivity, HomeActivity::class.java).also { intent ->
                            intent.putExtra(TOKEN, it)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }, TIME_SPLASH.toLong())
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            startDelay = 500
        }.start()
    }
}