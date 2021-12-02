package com.streamlabs.test.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamlabs.entity.data.IVideoRepository
import com.streamlabs.entity.model.Video
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class Intent {
    object LoadVideos : Intent()
    object Refresh : Intent()
    data class Like(val id: Long) : Intent()
}

sealed class State {
    data class Data(val videos: List<Video> = emptyList()) : State()
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

    private val _eventObservable = MutableLiveData<Event>()
    val eventObservable: LiveData<Event> = _eventObservable

    private val _stateObservable = MutableLiveData<State>()
    val stateObservable: LiveData<State> = _stateObservable

    private var _observeVideosJob: Job? = null
    private var _fetchVideosJob: Job? = null

    private fun loadVideos() {
        _stateObservable.postValue(State.Loading())
        _observeVideosJob?.cancel()
        _observeVideosJob = viewModelScope.launch(handler) {
            withContext(Dispatchers.IO) {
                repository.getVideos(true).collect {
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
        Intent.Refresh -> refresh()
        else -> throw IllegalArgumentException("Unknown intent: $intent")
    }
}