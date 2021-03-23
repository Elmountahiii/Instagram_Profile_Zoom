package com.redgunner.instagramzommy.network.retrofit

import com.redgunner.instagramzommy.network.api.InstagramSearchApi
import com.redgunner.instagramzommy.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val searchRetrofit by lazy {
        Retrofit.Builder().baseUrl(Constants.myServerURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val searchAPI:InstagramSearchApi by lazy {
        searchRetrofit.create(InstagramSearchApi::class.java)
    }
}