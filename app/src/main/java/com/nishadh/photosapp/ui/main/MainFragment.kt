package com.nishadh.photosapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.nishadh.photosapp.R
import com.nishadh.photosapp.databinding.MainFragmentBinding
import com.nishadh.photosapp.ui.home.adapter.PhotoCardViewAdapter
import com.nishadh.photosapp.ui.home.adapter.PhotoListViewAdapter
import com.nishadh.photosapp.ui.main.touchHelper.PhotoItemTouchHelperCallback
import com.nishadh.photosapp.util.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: MainFragmentBinding
    @Inject
    lateinit var appPreferences: AppPreferences

    // Stop bad animation during item swap
    private var ignoreUpdates = false
    private var scrollOnUpdate = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        setupList()
        return binding.root
    }

    private fun setupList() {
        if (appPreferences.showGridView) {
            binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
            val adapter = PhotoCardViewAdapter(this::onItemClicked, this::onItemMoved, this::onItemRemoved).apply {
                viewModel.photos.value?.let {
                    submitList(it.toMutableList())
                }
            }
            val itemTouchHelper = ItemTouchHelper(PhotoItemTouchHelperCallback(adapter))
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
            adapter.itemTouchHelper = itemTouchHelper
            binding.recyclerView.adapter = adapter
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(activity, 1)
            val adapter = PhotoListViewAdapter(this::onItemClicked, this::onItemMoved, this::onItemRemoved).apply {
                viewModel.photos.value?.let {
                    submitList(it.toMutableList())
                }
            }
            val itemTouchHelper = ItemTouchHelper(PhotoItemTouchHelperCallback(adapter))
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
            adapter.itemTouchHelper = itemTouchHelper
            binding.recyclerView.adapter = adapter
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }

        viewModel.photos.observe(viewLifecycleOwner, Observer {
            if (ignoreUpdates) {
                ignoreUpdates = false
            } else {
                val adapter = binding.recyclerView.adapter
                if (adapter is PhotoCardViewAdapter) {
                    adapter.submitList(it.toMutableList())
                } else if (adapter is PhotoListViewAdapter) {
                    adapter.submitList(it.toMutableList())
                }
                if (scrollOnUpdate) {
                    scrollOnUpdate = false
                    binding.recyclerView.smoothScrollToPosition(binding.recyclerView.adapter!!.itemCount);

                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        binding.toggleGridViewButton.setOnClickListener {
            appPreferences.showGridView = !appPreferences.showGridView
            setupList()
        }

        binding.toggleDarkModeButton.setOnClickListener {
            val mode =
                if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                    Configuration.UI_MODE_NIGHT_NO
                ) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
            // Change UI Mode
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        binding.fab.setOnClickListener {
            scrollOnUpdate = true
            viewModel.addPhoto()
        }
    }

    private fun onItemClicked(itemView: View, author: TextView, photo: PhotoUio) {
        viewModel.selectedPhoto.value = photo
        val photoCardDetailTransitionName = getString(R.string.photo_card_detail_transition_name)
        itemView.transitionName=  getString(R.string.photo_card_transition_name, photo.id)
        val extras: FragmentNavigator.Extras = FragmentNavigatorExtras(itemView to photoCardDetailTransitionName )
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(photo.id), extras)
    }

    private fun onItemMoved(fromPosition: Int, toPosition: Int)
    {
        ignoreUpdates = true
        viewModel.swapPhotoPosition(fromPosition, toPosition)
    }

    private fun onItemRemoved(position: Int) {
        viewModel.deletePhoto(position)
    }

}