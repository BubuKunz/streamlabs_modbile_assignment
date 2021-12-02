package com.streamlabs.data.api

import com.streamlabs.data.api.model.User
import com.streamlabs.data.api.model.Video
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface WebService {

    @GET("/videos")
    suspend fun getVideos(): Response<List<Video>>

    @GET("/users")
    suspend fun getUsers(@Query("ids") ids: Array<String>): Response<List<User>>

    @GET("/user")
    suspend fun getUser(@Query("id") ids: String): Response<User>
}