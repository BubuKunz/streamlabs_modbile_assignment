package com.streamlabs.data.mapper.local

import com.streamlabs.data.local.model.User as LocalUser
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.User

internal class LocalUserMapper : ModelMapper<User, LocalUser> {
    override fun fromModel(model: User): LocalUser {
        return LocalUser(
            id = model.id,
            userName = model.userName,
            userTitle = model.userTitle,
            userLocalImage = model.userLocalImage,
            userVideos = model.userVideos,
            userFollowing = model.userFollowing,
            userFans = model.userFans,
            userHearts = model.userHearts,
        )
    }

    override fun toModel(other: LocalUser): User {
        return User(
            id = other.id,
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