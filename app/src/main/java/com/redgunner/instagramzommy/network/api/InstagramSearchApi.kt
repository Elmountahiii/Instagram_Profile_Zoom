package com.redgunner.instagramzommy.network.api

import com.redgunner.instagramzommy.models.profile.AccountResponse
import com.redgunner.instagramzommy.models.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InstagramSearchApi {

    @GET("search/{userName}")
    suspend fun getSearchAccounts(@Path("userName")
        userName:String):Response<SearchResponse>


    @GET("account/{userName}")
    suspend fun getAccountProfile(@Path("userName") userName:String ):Response<AccountResponse>
}