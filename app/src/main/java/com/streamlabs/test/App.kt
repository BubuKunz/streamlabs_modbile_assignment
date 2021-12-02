package com.streamlabs.test

import android.app.Application
import com.streamlabs.data.DataProvider
import com.streamlabs.entity.data.IUserRepository
import com.streamlabs.entity.data.IVideoRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppServices.init(this)
    }

    object AppServices {
        fun init(application: Application) {
            val provider = DataProvider(application)
            userRepo = provider.userRepository
            videoRepo = provider.videoRepository
        }

        lateinit var userRepo: IUserRepository
        lateinit var videoRepo: IVideoRepository
    }
}