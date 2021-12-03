package com.streamlabs.test.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.streamlabs.test.R
import com.streamlabs.test.databinding.ViewVideoItemBinding

class VideosAdapter(private val player: ExoPlayer) :
    ListAdapter<VideoItem, VideosAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_video_item, viewGroup, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewVideoItemBinding.bind(view)

        fun bind(item: VideoItem) {
            if (item.playWhenReady) {
                binding.videoView.player = player
                player.addMediaItem(item.mediaItem)
                player.playWhenReady = item.playWhenReady
                player.seekTo(item.currentWindow, item.playbackPosition)
                player.prepare()
            } else {
                binding.videoView.player = null
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback : DiffUtil.ItemCallback<VideoItem>() {
    override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
        return oldItem.video == newItem.video
    }

    override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
        return oldItem == newItem
    }
}