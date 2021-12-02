package com.streamlabs.data.api.model

import com.google.gson.annotations.SerializedName

internal data class User(
    @SerializedName("user_name") val userName: String? = null,
    @SerializedName("user_title") val userTitle: String? = null,
    @SerializedName("user_local_image") val userLocalImage: String? = null,
    @SerializedName("user_videos") val userVideos: Int? = null,
    @SerializedName("user_following") val userFollowing: Int? = null,
    @SerializedName("user_fans") val userFans: Int? = null,
    @SerializedName("user_hearts") val userHearts: Int? = null
)