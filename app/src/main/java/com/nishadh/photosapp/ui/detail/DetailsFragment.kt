package com.nishadh.photosapp.ui.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.nishadh.photosapp.databinding.DetailsFragmentBinding
import com.nishadh.photosapp.ui.main.MainViewModel

class DetailsFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: DetailsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transformation: MaterialContainerTransform = MaterialContainerTransform().apply {
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
            setPathMotion(MaterialArcMotion())
            duration = 500
        }
        transformation.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                super.onTransitionEnd(transition)
                binding.textView.animate().alpha(1.0f)
            }
            override fun onTransitionStart(transition: Transition) {
                super.onTransitionStart(transition)
                binding.textView.alpha = 0.0f

            }
        })
        sharedElementEnterTransition = transformation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener { activity?.onBackPressed() })
        binding.textView.alpha = 0.0f
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition();
        binding.root.transitionName = getString(com.nishadh.photosapp.R.string.photo_card_detail_transition_name)
        viewModel.selectedPhoto.observe(viewLifecycleOwner, Observer {
            Glide.with(requireActivity()).load(it.imageUrl).into(binding.imageView);
            binding.toolbar.title = it.author
            startPostponedEnterTransition();
        })
    }

}
