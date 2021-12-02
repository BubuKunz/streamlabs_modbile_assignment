package com.streamlabs.data.local.model

internal data class User(
    val id: String?,
    val userName: String?,
    val userTitle: String?,
    val userLocalImage: String?,
    val userVideos: Int?,
    val userFollowing: Int?,
    val userFans: Int?,
    val userHearts: Int?
)