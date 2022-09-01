package com.dodi.cerita.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.dodi.cerita.R
import com.dodi.cerita.ui.activity.home.HomeActivity
import com.dodi.cerita.ui.activity.home.HomeActivity.Companion.TOKEN
import com.dodi.cerita.ui.activity.main.MainViewModel
import com.dodi.cerita.ui.activity.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.viewModelScope.launch {
            viewModel.getToken().collect{
                if (it.isNullOrEmpty()){
                    Intent(this@MainActivity,UserActivity::class.java).also { intent->
                        startActivity(intent)
                        finish()
                    }
                }else{
                    Intent(this@MainActivity,HomeActivity::class.java).also { intent->
                        intent.putExtra(TOKEN,it)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}