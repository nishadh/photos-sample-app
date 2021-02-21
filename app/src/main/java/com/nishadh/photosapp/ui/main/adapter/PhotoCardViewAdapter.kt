package com.nishadh.photosapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nishadh.photosapp.databinding.ItemPhotoCardViewBinding
import com.nishadh.photosapp.ui.main.viewholder.PhotoCardViewHolder
import com.nishadh.photosapp.ui.main.PhotoUio

class PhotoCardViewAdapter(
    private val onItemClicked: (View, TextView, PhotoUio) -> Unit
) : ListAdapter<PhotoUio, PhotoCardViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoCardViewHolder(
        ItemPhotoCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PhotoCardViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoUio>() {
            override fun areItemsTheSame(oldItem: PhotoUio, newItem: PhotoUio): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoUio, newItem: PhotoUio): Boolean =
                oldItem == newItem
        }
    }
}