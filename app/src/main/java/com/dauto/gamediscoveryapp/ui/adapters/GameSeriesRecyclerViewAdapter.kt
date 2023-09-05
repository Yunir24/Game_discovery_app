package com.dauto.gamediscoveryapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dauto.gamediscoveryapp.databinding.GameItemBinding
import com.dauto.gamediscoveryapp.domain.entity.Game

class GameSeriesRecyclerViewAdapter () :
    ListAdapter<Game, GamerRecyclerView.GameViewHolder>(SimpleCallback()) {
    class GameViewHolder(val binding: GameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game?, context: Context) {
            game?.let {
                binding.nameGameTV.text = it.name
                Glide
                    .with(context)
                    .load(it.backgroundImage)
                    .into(binding.posterIV)

            }
        }
    }

    var gameItemClickListener: ((Game) -> Unit)? = null

    class SimpleCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamerRecyclerView.GameViewHolder {
        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GamerRecyclerView.GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GamerRecyclerView.GameViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, holder.itemView.context)
            holder.itemView.setOnClickListener {
                gameItemClickListener?.invoke(item)
            }
        }
    }


}