package com.streamlabs.data.repo

import com.streamlabs.data.api.VideosRemoteDataHolder
import com.streamlabs.data.local.VideosLocalDataHolder
import com.streamlabs.entity.data.IVideoRepository
import com.streamlabs.entity.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

internal class VideoRepository(
    private val videosLocalDataHolder: VideosLocalDataHolder,
    private val videosRemoteDataHolder: VideosRemoteDataHolder,
) : IVideoRepository {
    override fun getVideos(fetch: Boolean): Flow<List<Video>> {
        return if (fetch) {
            videosLocalDataHolder.observeVideos().combine(
                flow {
                    fetchVideos()
                    emit(Unit)
                }
            ) { videos, _ ->
                videos
            }
        } else {
            videosLocalDataHolder.observeVideos()
        }
    }

    override suspend fun fetchVideos(): List<Video> {
        val apiVideos = videosRemoteDataHolder.getVideos()
        videosLocalDataHolder.updateVideos(apiVideos)
        return apiVideos
    }
}