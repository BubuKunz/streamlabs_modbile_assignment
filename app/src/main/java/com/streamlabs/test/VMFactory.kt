package com.streamlabs.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.streamlabs.test.feed.VideoFeedViewModel

class VMFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            VideoFeedViewModel::class.java -> VideoFeedViewModel(App.AppServices.videoRepo) as T
            else -> throw IllegalArgumentException("Factory doesn't know about ViewModel: $modelClass")
        }
    }
}