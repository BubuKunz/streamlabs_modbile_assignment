package com.streamlabs.test.feed

import com.google.android.exoplayer2.MediaItem
import com.streamlabs.entity.model.Video

data class VideoItem(
    var playWhenReady: Boolean = true,
    var currentWindow: Int = 0,
    var playbackPosition: Long = 0L,
    val mediaItem: MediaItem,
    val video: Video,
)