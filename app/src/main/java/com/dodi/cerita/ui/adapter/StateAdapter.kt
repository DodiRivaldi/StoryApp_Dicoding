package com.dodi.cerita.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dodi.cerita.databinding.ItemReloadAdapterBinding

class StateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<StateAdapter.StateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StateViewHolder {
        val binding =
            ItemReloadAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: StateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class StateViewHolder(private val binding: ItemReloadAdapterBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                btnRetry.setOnClickListener {
                    retry.invoke()
                }
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {

            }
            binding.btnRetry.isVisible = loadState is LoadState.Error

        }
    }


}