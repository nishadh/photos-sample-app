package com.nishadh.photosapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nishadh.photosapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}