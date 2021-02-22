package com.nishadh.photosapp.ui.main.viewholder

import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nishadh.photosapp.R
import com.nishadh.photosapp.ui.main.PhotoUio
import com.nishadh.photosapp.databinding.ItemPhotoListViewBinding

class PhotoListViewHolder(private val binding: ItemPhotoListViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(itemTouchHelper: ItemTouchHelper,
             photo: PhotoUio, onItemClicked: (View, TextView, PhotoUio) -> Unit) {
        binding.author.text = photo.author

        Glide.with(binding.imageView.context).load(photo.imageUrl).into(binding.imageView);

        itemView.transitionName = itemView.context.getString(R.string.photo_card_transition_name, photo.id)

        binding.root.setOnClickListener {
            onItemClicked(itemView, binding.author, photo)
        }

        binding.dragHandle.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    itemTouchHelper.startDrag(this@PhotoListViewHolder)
                    true
                }
            }
            false
        }
    }
}