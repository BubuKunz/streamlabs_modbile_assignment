package com.streamlabs.data.local

import com.streamlabs.data.local.model.Video as VideoLocal
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal interface IVideosLocalDataHolder {
    suspend fun updateVideos(videos: List<Video>): Boolean
    fun observeVideos(): Flow<List<Video>>
    suspend fun getVideos(): List<Video>
}

internal class VideosLocalDataHolder(
    private val localModelMapper: ModelMapper<Video, VideoLocal>,
) : IVideosLocalDataHolder {
    private val videosFlow = MutableStateFlow<List<VideoLocal>>(emptyList())
    override suspend fun updateVideos(videos: List<Video>): Boolean {
        videosFlow.value = videos.map {
            localModelMapper.fromModel(it)
        }
        return true
    }

    override fun observeVideos(): Flow<List<Video>> = videosFlow.map { list ->
        list.map { localModelMapper.toModel(it) }
    }

    override suspend fun getVideos(): List<Video> = videosFlow.value.map {
        localModelMapper.toModel(it)
    }
}