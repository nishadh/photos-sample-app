package com.nishadh.photosapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import com.nishadh.photosapp.databinding.ItemPhotoCardViewBinding
import com.nishadh.photosapp.ui.main.viewholder.PhotoCardViewHolder
import com.nishadh.photosapp.ui.main.PhotoUio
import com.nishadh.photosapp.ui.main.touchHelper.ItemTouchHelperAdapter

class PhotoCardViewAdapter(
    private val onItemClicked: (View, TextView, PhotoUio) -> Unit,
    private val onItemMoved: (fromPosition: Int, toPosition: Int) -> Unit,
    private val onItemRemoved: (position: Int) -> Unit
) : ListAdapter<PhotoUio, PhotoCardViewHolder>(DIFF_CALLBACK), ItemTouchHelperAdapter {

    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoCardViewHolder(
        ItemPhotoCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PhotoCardViewHolder, position: Int) =
        holder.bind(itemTouchHelper, getItem(position), onItemClicked)

    override fun onMove(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onSwiped(position: Int) {
        onItemRemoved(position)
    }

    override fun onMoveCompleted(fromPosition: Int, toPosition: Int) {
        onItemMoved(fromPosition, toPosition)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoUio>() {
            override fun areItemsTheSame(oldItem: PhotoUio, newItem: PhotoUio): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoUio, newItem: PhotoUio): Boolean =
                oldItem == newItem
        }
    }

}