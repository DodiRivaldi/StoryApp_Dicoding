package com.dodi.cerita.ui.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dodi.cerita.R
import com.dodi.cerita.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}