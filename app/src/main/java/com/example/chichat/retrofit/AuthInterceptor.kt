package com.example.chichat.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req =chain.request()

        val request = req.newBuilder()
            .addHeader("Authorization","dMERrLouYkHfZDQlrqY8nj7w9bcLZQ744G8iGokuiDDAocnlKP6r541v")
            .build()

        return chain.proceed(request)
    }
}