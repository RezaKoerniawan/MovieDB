package com.example.core.network

import com.example.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.TMDB_API_KEY
        if (apiKey.isBlank() || apiKey == "YOUR_TMDB_API_KEY") {
            throw IOException(
                "TMDB_API_KEY is not configured. Add your key to the local .env file."
            )
        }

        val authenticatedUrl = chain.request().url.newBuilder()
            .setQueryParameter("api_key", apiKey)
            .build()

        val authenticatedRequest = chain.request().newBuilder()
            .url(authenticatedUrl)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
