package com.streamlabs.test

import android.app.Application
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.streamlabs.data.DataProvider
import com.streamlabs.entity.data.IUserRepository
import com.streamlabs.entity.data.IVideoRepository
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppServices.init(this)
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
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