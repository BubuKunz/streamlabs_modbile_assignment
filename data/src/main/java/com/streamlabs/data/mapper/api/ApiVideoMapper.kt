package com.streamlabs.data.mapper.api

import com.streamlabs.data.api.model.Video as ApiVideo
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.User
import com.streamlabs.entity.model.Video

internal class ApiVideoMapper : ModelMapper<Video, ApiVideo> {
    override fun fromModel(model: Video): ApiVideo {
        return ApiVideo(
            videoDescription = model.videoDescription,
            videoPath = model.videoPath,
            videoNumberLikes = model.videoNumberLikes,
            videoNumberComments = model.videoNumberComments,
            userId = model.user?.id,
            userName = model.user?.userName,
            userImagePath = model.user?.userLocalImage,
        )
    }

    override fun toModel(other: ApiVideo): Video {
        return Video(
            videoDescription = other.videoDescription,
            videoPath = other.videoPath,
            videoNumberLikes = other.videoNumberLikes,
            videoNumberComments = other.videoNumberComments,
            user = other.run {
                if (!userId.isNullOrBlank() || !userImagePath.isNullOrBlank() || !userName.isNullOrBlank()) {
                    User(
                        userName = userName,
                        userLocalImage = userImagePath,
                        id = userId,
                        userTitle = null,
                        userVideos = null,
                        userFollowing = null,
                        userFans = null,
                        userHearts = null,
                    )
                } else {
                    null
                }
            },
        )
    }
}