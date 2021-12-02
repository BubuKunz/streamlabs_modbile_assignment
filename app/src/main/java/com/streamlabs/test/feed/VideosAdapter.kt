package com.streamlabs.test.feed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.streamlabs.entity.model.Video
import com.streamlabs.test.R
import com.streamlabs.test.databinding.ViewVideoItemBinding

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    private var items = listOf<Video>()

    @SuppressLint("NotifyDataSetChanged")
    fun submit(items: List<Video>) {
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

        fun bind(item: Video) {
            binding.textView.text = item.videoDescription
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}