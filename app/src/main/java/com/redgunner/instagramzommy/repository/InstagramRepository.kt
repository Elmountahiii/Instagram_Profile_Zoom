package com.redgunner.instagramzommy.repository

import com.redgunner.instagramzommy.database.dao.InstagramDao
import com.redgunner.instagramzommy.models.login.LoginResponse
import com.redgunner.instagramzommy.models.profile.AccountResponse
import com.redgunner.instagramzommy.models.search.SearchResponse
import com.redgunner.instagramzommy.models.search.UserX

import com.redgunner.instagramzommy.network.retrofit.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstagramRepository @Inject constructor(private val instagramDao: InstagramDao) {


    val favoriteAccountList=instagramDao.getFavoriteAccounts()

    val historySearchList=instagramDao.getHistoryAccounts()



    suspend fun search(userName: String): Response<SearchResponse> {


        return RetrofitInstance.searchAPI.getSearchAccounts(userName)
    }

    suspend fun addAccount(account: UserX){
        instagramDao.addAccount(account)
    }


    suspend fun getInstagramAccount(userName: String):Response<AccountResponse>{
        return RetrofitInstance.searchAPI.getAccountProfile(userName)

    }

    suspend fun startServer():Response<LoginResponse>{
        return RetrofitInstance.searchAPI.startServer()
    }
}