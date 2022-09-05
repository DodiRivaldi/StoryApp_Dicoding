package com.dodi.cerita.ui.activity.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.showProgressBar
import com.dodi.cerita.data.local.db.CeritaItem
import com.dodi.cerita.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item : CeritaItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        item = intent.getParcelableExtra(TOKEN)!!
        setSupportActionBar(binding.toolbarDetail)
        binding.apply {
            showProgressBar(true,pbDetail)
            Glide.with(this@DetailActivity).load(item.photoUrl).into(img)
            tvDesc.text = item.description
            tvUser.text = item.name
            showProgressBar(false,pbDetail)
        }
    }

    companion object{
        const val TOKEN = "TOKEN"
    }
}