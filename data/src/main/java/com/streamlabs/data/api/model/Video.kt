package com.streamlabs.data.api.model

import com.google.gson.annotations.SerializedName

internal data class Video(

    @SerializedName("video_description") val videoDescription: String? = null,
    @SerializedName("video_path") val videoPath: String? = null,
    @SerializedName("video_number_likes") val videoNumberLikes: Int? = null,
    @SerializedName("video_number_comments") val videoNumberComments: Int? = null,
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("user_name") val userName: String? = null,
    @SerializedName("user_image_path") val userImagePath: String? = null
)