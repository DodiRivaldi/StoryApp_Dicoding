package com.dodi.cerita.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dodi.cerita.data.local.db.CeritaItem
import com.dodi.cerita.databinding.ItemCeritaBinding
import com.dodi.cerita.ui.activity.detail.DetailActivity
import com.dodi.cerita.ui.activity.detail.DetailActivity.Companion.TOKEN

class CeritaAdapter : PagingDataAdapter<CeritaItem, CeritaAdapter.ViewHolder>(CALLBACK) {

    class ViewHolder(private val binding: ItemCeritaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, cerita: CeritaItem) {
            binding.apply {
                Glide.with(context).load(cerita.photoUrl).into(imgStory)
                tvUser.text = cerita.name
                tvDesc.text = cerita.description

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(imgStory, "image"),
                            Pair(tvUser, "username"),
                            Pair(tvDesc, "description")
                        )

                    Intent(context, DetailActivity::class.java).also {
                        it.putExtra(TOKEN, cerita)
                        context.startActivity(it, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<CeritaItem> =
            object : DiffUtil.ItemCallback<CeritaItem>() {
                override fun areItemsTheSame(
                    oldItem: CeritaItem,
                    newItem: CeritaItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CeritaItem,
                    newItem: CeritaItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.itemView.context, getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCeritaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}