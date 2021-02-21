package com.nishadh.photosapp.ui.main.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nishadh.photosapp.ui.main.PhotoUio
import com.nishadh.photosapp.databinding.ItemPhotoListViewBinding

class PhotoListViewHolder(private val binding: ItemPhotoListViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotoUio, onItemClicked: (View, TextView, PhotoUio) -> Unit) {
        binding.author.text = photo.author

        Glide.with(binding.imageView.context).load(photo.imageUrl).into(binding.imageView);


        binding.root.setOnClickListener {
            onItemClicked(itemView, binding.author, photo)
        }

        binding.author.transitionName = "author_${photo.id}"
        itemView.transitionName = "card_${photo.id}"
    }
}