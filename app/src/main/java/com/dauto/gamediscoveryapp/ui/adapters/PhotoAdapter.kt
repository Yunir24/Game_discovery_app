package com.dauto.gamediscoveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dauto.gamediscoveryapp.databinding.GameImageItemBinding
import com.dauto.gamediscoveryapp.domain.entity.Game

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    val listP = mutableListOf<String>()

    class PhotoViewHolder(val binding: GameImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            GameImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = listP[position]
        with(holder) {
            Glide.with(holder.itemView.context).load(item).into(
                binding.imageViewCarousel
            )
        }
    }

    override fun getItemCount(): Int = listP.size

    fun addList(list: List<String>) {
        listP.addAll(list)
    }

}