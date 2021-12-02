package com.streamlabs.entity.data

import com.streamlabs.entity.model.Video
import kotlinx.coroutines.flow.Flow

interface IVideoRepository {

    /**
     * @param fetch - if true receive videos from network and start observing cache, otherwise
     * only observes cache
     * @throws EntityError.kt in case of any errors (including HTTP errors)
     * @return obserbale list of videos
     */
    fun getVideos(fetch: Boolean): Flow<List<Video>>

    /**
     * Fetches videos from network and store it to cache
     * @throws EntityError.kt in case of any errors (including HTTP errors)
     * @return list of videos
     */
    suspend fun fetchVideos(): List<Video>
}