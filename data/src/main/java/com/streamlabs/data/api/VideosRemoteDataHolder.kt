package com.streamlabs.data.api

import com.streamlabs.data.api.model.Video as VideoApi
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.Video

internal interface IVideosRemoteDataHolder {
    suspend fun getVideos(): List<Video>
}

internal class VideosRemoteDataHolder(
    private val webService: WebService,
    private val apiModelMapper: ModelMapper<Video, VideoApi>,
) : IVideosRemoteDataHolder {
    override suspend fun getVideos(): List<Video> {
        return safeApiCall {
            webService.getVideos()
        }.let { users ->
            users.map {
                apiModelMapper.toModel(it)
            }
        }
    }
}