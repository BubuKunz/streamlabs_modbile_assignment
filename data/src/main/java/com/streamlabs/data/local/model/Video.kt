package com.streamlabs.data.local.model

internal data class Video(

    val videoDescription: String?,
    val videoPath: String?,
    val videoNumberLikes: Int?,
    val videoNumberComments: Int?,
    val userId: String?,
    val userName: String?,
    val userImagePath: String?
)