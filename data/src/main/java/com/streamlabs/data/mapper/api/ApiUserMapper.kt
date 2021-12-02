package com.streamlabs.data.mapper.api

import com.streamlabs.data.api.model.User as ApiUser
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.User

internal class ApiUserMapper : ModelMapper<User, ApiUser> {
    override fun fromModel(model: User): ApiUser {
        return ApiUser(
            userName = model.userName,
            userTitle = model.userTitle,
            userLocalImage = model.userLocalImage,
            userVideos = model.userVideos,
            userFollowing = model.userFollowing,
            userFans = model.userFans,
            userHearts = model.userHearts,
        )
    }

    override fun toModel(other: ApiUser): User {
        return User(
            id = null,
            userName = other.userName,
            userTitle = other.userTitle,
            userLocalImage = other.userLocalImage,
            userVideos = other.userVideos,
            userFollowing = other.userFollowing,
            userFans = other.userFans,
            userHearts = other.userHearts,
        )
    }
}