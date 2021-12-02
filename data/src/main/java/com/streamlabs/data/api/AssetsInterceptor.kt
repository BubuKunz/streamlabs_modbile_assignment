package com.streamlabs.data.api

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class AssetsInterceptor(appContext: Context) : Interceptor {
    private val assets = appContext.assets
    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url.toUri().toString()
        val responseString = when {
            uri.endsWith("videos") -> "videos.json".loadJSONFromAsset() ?: ""
            uri.contains("user") -> "user.json".loadJSONFromAsset() ?: ""
            else -> ""
        }

        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(responseString)
            .body(
                ResponseBody.create(
                    "application/json".toMediaTypeOrNull(),
                    responseString.toByteArray()
                )
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    fun String.loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = assets.open(this)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}