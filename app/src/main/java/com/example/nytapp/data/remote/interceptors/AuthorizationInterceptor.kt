package com.example.nytapp.data.remote.interceptors

import com.example.nytapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val originalHttpUrl = request.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api-key", BuildConfig.API_KEY)
            .build()
        val requestBuilder = request.newBuilder()
            .url(url)

        request = requestBuilder.build()
        return chain.proceed(request)
    }
}