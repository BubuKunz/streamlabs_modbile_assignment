package com.streamlabs.entity.model

data class Video(
    val videoDescription: String?,
    val videoPath: String?,
    val videoNumberLikes: Int?,
    val videoNumberComments: Int?,
    val user: User?,
)