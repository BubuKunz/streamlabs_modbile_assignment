package com.streamlabs.test.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.streamlabs.test.R
import com.streamlabs.test.VMFactory
import com.streamlabs.test.databinding.FragmentVideosFeedBinding
import okhttp3.OkHttpClient

class VideoFeedFragment : Fragment(R.layout.fragment_videos_feed) {
    private val playbackStateListener: Player.EventListener = playbackStateListener()
    private val player by lazy {
        initializePlayer()
    }
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private val adapter by lazy {
        VideosAdapter(player)
    }

    private fun initializePlayer(): ExoPlayer {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
        val trackSelector = DefaultTrackSelector(requireContext()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        val mediaSourceFactory: MediaSourceFactory =
            DefaultMediaSourceFactory(OkHttpDataSource.Factory(okHttpClient))

        return ExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().also {
                it.addListener(playbackStateListener)
            }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            removeListener(playbackStateListener)
            release()
        }
//        player = null
    }

    override fun onStop() {
        player.stop()
        super.onStop()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

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
        snapHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        var position = 0
        binding.recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            var newPosition =
                (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (position != newPosition) {
                // todo fix playbackPosition detecting
                viewModel.onIntent(
                    Intent.PlayPositionChanged(
                        newPosition,
                        position,
                        player.contentPosition
                    )
                )
                position = newPosition
            }
        }
        with(viewModel) {
            onIntent(Intent.LoadVideos)
            stateObservable.observe(this@VideoFeedFragment) {
                when (it) {
                    is State.Data -> adapter.submitList(it.videos)
                }
            }
        }
    }
}

private fun playbackStateListener() = object : Player.EventListener {

    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d("TestTAG", "changed state to $stateString")
    }
}