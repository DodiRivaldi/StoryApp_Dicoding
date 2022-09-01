package com.dodi.cerita.ui.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dodi.cerita.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    companion object {
        const val TOKEN = "token"
    }
}