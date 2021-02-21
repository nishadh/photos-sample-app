package com.nishadh.photosapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nishadh.photosapp.R
import com.nishadh.photosapp.databinding.MainFragmentBinding
import com.nishadh.photosapp.ui.home.adapter.PhotoCardViewAdapter
import com.nishadh.photosapp.ui.home.adapter.PhotoListViewAdapter
import com.nishadh.photosapp.util.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: MainFragmentBinding
    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        submitList()
        return binding.root
    }

    private fun showMockData() {
        viewModel.photos.value = arrayOf(
            PhotoUio(
                "1",
                "Matthew Wiebe",
                "https://picsum.photos/id/1025/4951/3301"
            ),
            PhotoUio(
                "2",
                "Мартин Тасев",
                "https://picsum.photos/id/1024/1920/1280"
            ),
            PhotoUio(
                "3",
                "William Hook",
                "https://picsum.photos/id/1023/3955/2094"
            ),
            PhotoUio(
                "4",
                "Vashishtha Jogi",
                "https://picsum.photos/id/1022/6000/3376"
            ),
            PhotoUio(
                "5",
                "Frances Gunn",
                "https://picsum.photos/id/1021/2048/1206"
            ),
            PhotoUio(
                "6",
                "Adam Willoughby-Knox",
                "https://picsum.photos/id/1020/4288/2848"
            ),
            PhotoUio(
                "7",
                "Ben Moore",
                "https://picsum.photos/id/102/4320/3240"
            )
        )
    }


    private fun submitList() {
        if (appPreferences.showGridView) {
            binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
            binding.recyclerView.adapter = PhotoCardViewAdapter(this::onItemClicked).apply {
                viewModel.photos.value?.let {
                    submitList(it.toMutableList())
                }
            }
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(activity, 1)
            binding.recyclerView.adapter = PhotoListViewAdapter(this::onItemClicked).apply {
                viewModel.photos.value?.let {
                    submitList(it.toMutableList())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.photos.observe(viewLifecycleOwner, Observer {
            val adapter = binding.recyclerView.adapter
            if (adapter is PhotoCardViewAdapter) {
                adapter.submitList(it.toMutableList())
            } else if (adapter is PhotoListViewAdapter) {
                adapter.submitList(it.toMutableList())
            }
        })

        binding.toggleGridViewButton.setOnClickListener {
            appPreferences.showGridView = !appPreferences.showGridView
            submitList()
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
        }

        showMockData()
    }

    private fun onItemClicked(itemView: View, author: TextView, photo: PhotoUio) {
        viewModel.selectedPhoto.value = photo
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(photo.id))
    }

}