package com.streamlabs.data.mapper.local

import com.streamlabs.data.local.model.Video as LocalVideo
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.User
import com.streamlabs.entity.model.Video

internal class LocalVideoMapper : ModelMapper<Video, LocalVideo> {
    override fun fromModel(model: Video): LocalVideo {
        return LocalVideo(
            videoDescription = model.videoDescription,
            videoPath = model.videoPath,
            videoNumberLikes = model.videoNumberLikes,
            videoNumberComments = model.videoNumberComments,
            userId = model.user?.id,
            userName = model.user?.userName,
            userImagePath = model.user?.userLocalImage,
        )
    }

    override fun toModel(other: LocalVideo): Video {
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