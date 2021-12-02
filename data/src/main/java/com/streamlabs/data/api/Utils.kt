package com.streamlabs.data.api

import android.content.Context
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.streamlabs.data.BuildConfig
import com.streamlabs.entity.error.AuthError
import com.streamlabs.entity.error.GenericError
import com.streamlabs.entity.error.NetworkError
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object NetworkUtils {

    private var api: WebService? = null

    fun getApi(
        context: Context,
    ): WebService {
        if (api == null) api = getWebService(context)
        return api!!
    }

    private fun provideHttpClient(
        context: Context,
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(AssetsInterceptor(context))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loginInterception = HttpLoggingInterceptor()
            loginInterception.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loginInterception)
        }

        return builder
            .build()
    }

    private fun getWebService(
        context: Context
    ): WebService {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder()
            .client(provideHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://com.mobile.assignment")
            .build()
        return retrofit.create(WebService::class.java)
    }
}

internal inline fun <T> safeApiCall(call: () -> Response<T>): T {

    return try {
        val resp = call.invoke()

        if (resp.isSuccessful) {
            resp.body() as T
        } else if (resp.code() == 401 || resp.code() == 403) {
            throw AuthError(code = resp.code(), error = resp.message())
        } else {
            val parsedError = parseError(resp.code(), resp.errorBody())
            throw (parsedError ?: GenericError(code = resp.code(), error = resp.message()))
        }
    } catch (throwable: Throwable) {
        Log.d("NETWORK_ERROR", "throwable ${throwable.message}")
        throw NetworkError
    }
}

fun parseError(code: Int?, errorBody: ResponseBody?): GenericError? {
    if (errorBody != null) {
        return try {
            val resp = Gson().fromJson(errorBody.charStream(), ErrorMessage::class.java)
            GenericError(code, resp.message, resp.errors.toString())
        } catch (e: Exception) {
            null
        }
    }
    return null
}