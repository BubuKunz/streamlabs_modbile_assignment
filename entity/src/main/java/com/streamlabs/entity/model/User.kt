package com.streamlabs.entity.model

data class User(
    val userName: String?,
    val userTitle: String?,
    val userLocalImage: String?,
    val userVideos: Int?,
    val userFollowing: Int?,
    val userFans: Int?,
    val userHearts: Int?
)