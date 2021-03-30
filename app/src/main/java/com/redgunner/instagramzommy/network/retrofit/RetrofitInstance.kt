package com.redgunner.instagramzommy.network.retrofit

import com.redgunner.instagramzommy.network.api.InstagramSearchApi
import com.redgunner.instagramzommy.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {

    private var okHttpClient= OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    private val searchRetrofit by lazy {
        Retrofit.Builder().baseUrl(Constants.myServerURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val searchAPI:InstagramSearchApi by lazy {
        searchRetrofit.create(InstagramSearchApi::class.java)
    }
}