package com.dauto.gamediscoveryapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dauto.gamediscoveryapp.databinding.GameItemBinding
import com.dauto.gamediscoveryapp.domain.entity.Game
import com.dauto.gamediscoveryapp.domain.entity.ParentPlatforms
import java.util.*

class GamerRecyclerView() :
    PagingDataAdapter<Game, GamerRecyclerView.GameViewHolder>(SimpleCallback()) {
    class GameViewHolder(val binding: GameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game?, context: Context) {
            game?.let { it ->
                binding.nameGameTV.text = it.name
                Glide
                    .with(context)
                    .load(it.backgroundImage)
                    .into(binding.posterIV)
                setIconVisibility(it)
            }
        }


        private fun setIconVisibility(game: Game) {
            val platformResult = game.platforms
            val parentPlatfromList =
                ParentPlatforms.values()
            parentPlatfromList.forEach { platform ->
                val visibility =
                    if (platform !in platformResult) View.GONE else View.VISIBLE
                when (platform) {
                    ParentPlatforms.PC -> binding.PCImage.visibility = visibility
                    ParentPlatforms.PLAYSTATION -> binding.PSImage.visibility = visibility
                    ParentPlatforms.XBOX -> binding.XBOXImage.visibility = visibility
                    ParentPlatforms.IOS -> binding.IOSImage.visibility = visibility
                    ParentPlatforms.ANDROID -> binding.AndroidImage.visibility = visibility
                    ParentPlatforms.APPLE_MACINTOSH -> binding.MacImage.visibility = visibility
                    ParentPlatforms.LINUX -> binding.LinuxImage.visibility = visibility
                    ParentPlatforms.NINTENDO -> binding.NintendoImage.visibility = visibility
                    ParentPlatforms.SEGA -> binding.SegaImage.visibility = visibility
                    ParentPlatforms.WEB -> binding.webImage.visibility = visibility
                }

            }
        }
    }

    var gameItemClickListener: ((Game) -> Unit)? = null

    class SimpleCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, holder.itemView.context)
            holder.itemView.setOnClickListener {
                gameItemClickListener?.invoke(item)
            }
        }
    }


}