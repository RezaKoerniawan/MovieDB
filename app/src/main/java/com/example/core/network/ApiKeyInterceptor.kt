package com.example.core.network

import okhttp3.Interceptor
import okhttp3.Response
import com.example.BuildConfig

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        
        val apiKey = BuildConfig.TMDB_API_KEY

        val url = originalUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
            
        val request = originalRequest.newBuilder()
            .url(url)
            .build()
            
        return chain.proceed(request)
    }
}
