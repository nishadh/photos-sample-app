package com.nishadh.photosapp.ui.main.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nishadh.photosapp.R
import com.nishadh.photosapp.databinding.ItemPhotoCardViewBinding

import com.nishadh.photosapp.ui.main.PhotoUio

class PhotoCardViewHolder(private val binding: ItemPhotoCardViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotoUio, onItemClicked: (View, TextView, PhotoUio) -> Unit) {
        binding.author.text = photo.author

        Glide.with(itemView.context).load(photo.imageUrl).into(binding.imageView);


        binding.root.setOnClickListener {
            onItemClicked(itemView, binding.author, photo)
        }
    }
}