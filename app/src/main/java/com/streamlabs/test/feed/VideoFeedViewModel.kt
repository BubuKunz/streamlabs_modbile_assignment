package com.streamlabs.test.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.streamlabs.entity.data.IVideoRepository
import com.streamlabs.entity.model.Video
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class Intent {
    data class PlayPositionChanged(
        val newPosition: Int,
        val oldPosition: Int,
        val oldItemPlaybackPosition: Long
    ) :
        Intent()

    object LoadVideos : Intent()
    object Refresh : Intent()
    data class Like(val id: Long) : Intent()
}

sealed class State {
    data class Data(val videos: List<VideoItem> = emptyList()) : State()
    data class Loading(val data: Data = Data()) : State()
    data class Refreshing(val data: Data = Data()) : State()
}

sealed class Event {
    object ShowError : Event()
    object ShowUserDetail : Event()
}

class VideoFeedViewModel(
    private val repository: IVideoRepository
) : ViewModel() {
    val handler = CoroutineExceptionHandler { context, exception ->
        _eventObservable.postValue(Event.ShowError)
        println("Caught $exception")
    }

    private var videos: List<VideoItem> = emptyList()
    private val _eventObservable = MutableLiveData<Event>()
    val eventObservable: LiveData<Event> = _eventObservable

    private val _stateObservable = MutableLiveData<State>()
    val stateObservable: LiveData<State> = _stateObservable

    private var _observeVideosJob: Job? = null
    private var _fetchVideosJob: Job? = null

    private fun loadVideos() {
        _stateObservable.postValue(State.Loading())
        _observeVideosJob?.cancel()
        val itemsMap = mutableMapOf<Video, VideoItem>()
        _observeVideosJob = viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                repository.getVideos(true)
                    .map { videos ->
                        videos.mapIndexed { i, it ->
                            if (!itemsMap.containsKey(it)) {
                                val mediaItem =
                                    MediaItem.fromUri("https://r4---sn-p5qlsny6.googlevideo.com/videoplayback?expire=1638547475&ei=s-upYZWxFMf71gLfnZ6QCg&ip=54.163.67.235&id=o-AGy_waX3fCM9-nHvjmQY1axBzDGruxZde2l0B3FgI8V9&itag=18&source=youtube&requiressl=yes&mh=Kc&mm=31%2C26&mn=sn-p5qlsny6%2Csn-vgqsrnlk&ms=au%2Conr&mv=u&mvi=4&pl=23&vprv=1&mime=video%2Fmp4&ns=eoNTtQfaJTQEo_7bQzY0i_cG&gir=yes&clen=235260405&ratebypass=yes&dur=5121.068&lmt=1638450547634097&mt=1638525557&fvip=4&fexp=24001373%2C24007246&c=WEB&txp=5530432&n=6gCicYLGrKCZgQ&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIhANcy3eEIqryWMQndJrNejnuj3PTHNeXpVhDHdSCuKWZ6AiAJnyfAoU4dJcSZTk5nTYftfljya80oMELR-lwK7JAeZQ%3D%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl&lsig=AG3C_xAwRQIhAJxFW70MxBjhcdRAIJ5uxg5Inrv5870bH6b4G1oufXVUAiBqsT-QaeSH02OQaH-mkky8q1lRXrRJ_uagXny3NCYLCA%3D%3D")
                                val item = VideoItem(
                                    video = it,
                                    mediaItem = mediaItem,
                                    playWhenReady = i == 0,
                                )
                                itemsMap[it] = item
                            }

                            itemsMap[it]!!
                        }.also {
                            this@VideoFeedViewModel.videos = it
                        }
                    }
                    .collect {
                        _stateObservable.postValue(State.Data(it))
                    }
            }
        }
    }

    private fun refresh() {
        val data = _stateObservable.value as? State.Data
        _stateObservable.postValue(State.Refreshing(data ?: State.Data()))
        _fetchVideosJob?.cancel()
        _fetchVideosJob = viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                repository.fetchVideos()
            }
        }
    }

    fun onIntent(intent: Intent) = when (intent) {
        Intent.LoadVideos -> loadVideos()
        is Intent.PlayPositionChanged -> updatePlayPosition(intent)
        Intent.Refresh -> refresh()
        else -> throw IllegalArgumentException("Unknown intent: $intent")
    }

    private fun updatePlayPosition(data: Intent.PlayPositionChanged) {
        videos.mapIndexed { index, videoItem ->
            if (index == data.oldPosition) {
                videoItem.copy(
                    playWhenReady = false,
                    playbackPosition = data.oldItemPlaybackPosition
                )
            } else {
                videoItem.copy(playWhenReady = index == data.newPosition)
            }
        }.let {
            _stateObservable.postValue(State.Data(it))
        }
    }
}