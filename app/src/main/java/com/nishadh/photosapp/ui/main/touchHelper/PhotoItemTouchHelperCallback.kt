package com.nishadh.photosapp.ui.main.touchHelper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperAdapter {
    fun onMove(fromPosition: Int, toPosition: Int)
    fun onSwiped(position: Int)
    fun onMoveCompleted(fromPosition: Int, toPosition: Int)
}

class PhotoItemTouchHelperCallback(adapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback() {

    private var adapter: ItemTouchHelperAdapter = adapter
    private var isMoving = false
    private var selectedInitialPosition = RecyclerView.NO_POSITION

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        isMoving = true
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        adapter.onMove(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.onSwiped(position)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.let {
            selectedInitialPosition = it.adapterPosition
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (isMoving) {
            val selectedFinalPosition = viewHolder.adapterPosition
            if (selectedInitialPosition != RecyclerView.NO_POSITION &&
                selectedFinalPosition != RecyclerView.NO_POSITION &&
                selectedInitialPosition != selectedFinalPosition) {
                adapter.onMoveCompleted(selectedInitialPosition, selectedFinalPosition)
            }
        }
        isMoving = false
    }
}