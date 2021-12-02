package com.streamlabs.test.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.streamlabs.test.R
import com.streamlabs.test.VMFactory
import com.streamlabs.test.databinding.FragmentVideosFeedBinding

class VideoFeedFragment : Fragment(R.layout.fragment_videos_feed) {

    private lateinit var binding: FragmentVideosFeedBinding
    private val viewModel by viewModels<VideoFeedViewModel> {
        VMFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.also {
            binding = FragmentVideosFeedBinding.bind(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val snapHelper: SnapHelper = PagerSnapHelper()
        val adapter = VideosAdapter()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        with(viewModel) {
            onIntent(Intent.LoadVideos)
            stateObservable.observe(this@VideoFeedFragment) {
                when (it) {
                    is State.Data -> adapter.submit(it.videos)
                }
            }
        }
    }
}