package com.dodi.cerita.ui.activity.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.showProgressBar
import com.dodi.cerita.databinding.ActivityHomeBinding
import com.dodi.cerita.ui.activity.location.LocationActivity
import com.dodi.cerita.ui.activity.upload.UploadActivity
import com.dodi.cerita.ui.activity.user.UserActivity
import com.dodi.cerita.ui.adapter.CeritaAdapter
import com.dodi.cerita.ui.adapter.StateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvCerita: RecyclerView
    private lateinit var adapter: CeritaAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarHome)
        showProgressBar(true, binding.pbHome)

        adapter = CeritaAdapter()
        rvCerita = binding.rvStory
        rvCerita.layoutManager = LinearLayoutManager(this)
        rvCerita.adapter = adapter.withLoadStateFooter(
            footer = StateAdapter {
                adapter.retry()
            }
        )
        rvCerita.setHasFixedSize(true)

        viewModel.getDataStory(intent.getStringExtra(TOKEN)!!).observe(this) {
            adapter.submitData(lifecycle, it)
            showProgressBar(false, binding.pbHome)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                Intent(this, UploadActivity::class.java).also { startActivity(it) }
                return true
            }

            R.id.menu_signout -> {
                viewModel.signOut()
                Intent(this, UserActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                return true
            }
            R.id.menu_loc -> {
                Intent(this,LocationActivity::class.java).also { startActivity(it) }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val TOKEN = "token"
    }
}