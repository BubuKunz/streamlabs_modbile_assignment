package com.streamlabs.test.feed

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.streamlabs.test.R
import com.streamlabs.test.databinding.ViewVideoItemBinding

class VideosAdapter(private val player: ExoPlayer, private val context: Context) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    private var items = listOf<VideoItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun submit(items: List<VideoItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_video_item, viewGroup, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewVideoItemBinding.bind(view)

        fun bind(item: VideoItem) {
            binding.videoView.player = player
            player.addMediaItem(item.mediaItem)
            player.playWhenReady = item.playWhenReady
            player.seekTo(item.currentWindow, item.playbackPosition)
            player.prepare()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}