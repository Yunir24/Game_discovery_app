package com.dauto.gamediscoveryapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dauto.gamediscoveryapp.databinding.GameItemBinding
import com.dauto.gamediscoveryapp.domain.entity.Game

class GamerRecyclerView(val context: Context) :
    PagingDataAdapter<Game, GamerRecyclerView.GameViewHolder>(SimpleCallback()) {
    class GameViewHolder(val binding: GameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(path: Game?, context: Context) {
            path?.let {
                binding.nameGameTV.text = it.name
                Glide
                    .with(context)
                    .load(it.backgroundImage)
                    .into(binding.posterIV)

            }
        }
    }

    class SimpleCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id && oldItem.metacritic == newItem.metacritic
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}