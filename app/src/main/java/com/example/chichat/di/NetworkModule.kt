package com.example.chichat.di

import com.example.chichat.retrofit.AuthInterceptor
import com.example.chichat.retrofit.ReelFlowAPI
import com.example.chichat.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(provideOkHttpClient(authInterceptor))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
        val builder = OkHttpClient()
            .newBuilder()
            .addInterceptor(authInterceptor)

        val requestInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addNetworkInterceptor(requestInterceptor)

        return builder.build()
    }

    @Singleton
    @Provides
    fun providesSevayuAPI(retrofit: Retrofit): ReelFlowAPI {
        return retrofit.create(ReelFlowAPI::class.java)
    }
}