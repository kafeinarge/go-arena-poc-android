package com.kafein.turkcellsaha.data.api

import android.content.Context
import com.kafein.turkcellsaha.data.local.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRequestInterceptor @Inject constructor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        SharedPrefs.init(context)
        val token = SharedPrefs.readToken()
        val httpUrl = request.url.newBuilder().build()
        request = request.newBuilder().url(httpUrl)
            .addHeader("Authorization", "Bearer " +token.toString())
            .addHeader("Content-Type", "application/json")
            .build()

        return chain.proceed(request)
    }

    companion object {
        const val TOKEN = "Token"
    }
}