package com.nishadh.photosapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nishadh.photosapp.databinding.ItemPhotoListViewBinding
import com.nishadh.photosapp.ui.main.viewholder.PhotoListViewHolder
import com.nishadh.photosapp.ui.main.PhotoUio

class PhotoListViewAdapter(
    private val onItemClicked: (View, TextView, PhotoUio) -> Unit
) : ListAdapter<PhotoUio, PhotoListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoListViewHolder(
        ItemPhotoListViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) =
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