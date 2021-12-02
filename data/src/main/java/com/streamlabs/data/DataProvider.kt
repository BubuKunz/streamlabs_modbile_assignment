package com.streamlabs.data

import android.content.Context
import com.streamlabs.data.api.NetworkUtils
import com.streamlabs.data.api.UsersRemoteDataHolder
import com.streamlabs.data.api.VideosRemoteDataHolder
import com.streamlabs.data.local.UsersLocalDataHolder
import com.streamlabs.data.local.VideosLocalDataHolder
import com.streamlabs.data.mapper.api.ApiUserMapper
import com.streamlabs.data.mapper.api.ApiVideoMapper
import com.streamlabs.data.mapper.local.LocalUserMapper
import com.streamlabs.data.mapper.local.LocalVideoMapper
import com.streamlabs.data.repo.UserRepository
import com.streamlabs.data.repo.VideoRepository
import com.streamlabs.entity.data.IUserRepository
import com.streamlabs.entity.data.IVideoRepository

class DataProvider(appContext: Context) {
    private val webService by lazy {
        NetworkUtils.getApi(appContext)
    }

    private val userLocalDataHolder = UsersLocalDataHolder(
        LocalUserMapper()
    )
    private val videosLocalDataHolder = VideosLocalDataHolder(
        LocalVideoMapper()
    )
    private val videosRemoteDataHolder = VideosRemoteDataHolder(
        webService,
        ApiVideoMapper()
    )
    private val userRemoteDataHolder = UsersRemoteDataHolder(
        webService,
        ApiUserMapper()
    )

    private val localUserMapper = LocalUserMapper()
    private val localVideoMapper = LocalVideoMapper()
    private val apiVideoMapper = ApiVideoMapper()
    private val apiUserMapper = ApiUserMapper()

    val vidRepository: IVideoRepository = VideoRepository(
        videosLocalDataHolder,
        videosRemoteDataHolder,
    )
    val userRepository: IUserRepository = UserRepository(
        userLocalDataHolder, userRemoteDataHolder
    )
}