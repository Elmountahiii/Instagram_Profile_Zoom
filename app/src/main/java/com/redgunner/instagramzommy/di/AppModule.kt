package com.redgunner.instagramzommy.di

import android.app.Application
import android.content.Context
import com.redgunner.instagramzommy.R
import com.redgunner.instagramzommy.network.api.InstagramSearchApi
import com.redgunner.instagramzommy.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {





    @Provides
    @Singleton
    fun provideServerURL(@ApplicationContext context: Context)=context.getString(R.string.ServerUrl)

    @Provides
    @Singleton
    fun  provideOkHttpClient():OkHttpClient=
        OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()



    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,serverUrl :String): Retrofit=
        Retrofit.Builder().baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient )
            .build()


    @Provides
    @Singleton
    fun provideInstagramSearchApi(retrofit: Retrofit):InstagramSearchApi=retrofit.create(InstagramSearchApi::class.java)




}