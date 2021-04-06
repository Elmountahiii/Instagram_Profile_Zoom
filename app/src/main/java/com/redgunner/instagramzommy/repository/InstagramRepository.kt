package com.redgunner.instagramzommy.repository

import com.redgunner.instagramzommy.database.dao.InstagramDao
import com.redgunner.instagramzommy.models.profile.AccountResponse
import com.redgunner.instagramzommy.models.search.SearchResponse
import com.redgunner.instagramzommy.models.search.UserX
import com.redgunner.instagramzommy.network.api.InstagramSearchApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstagramRepository @Inject constructor(private val instagramDao: InstagramDao,private val instagramSearchApi: InstagramSearchApi) {


    val favoriteAccountList=instagramDao.getFavoriteAccounts()

    val historySearchList=instagramDao.getHistoryAccounts()



    suspend fun search(userName: String): Response<SearchResponse> {


        return instagramSearchApi.getSearchAccounts(userName)
    }

    suspend fun addAccount(account: UserX){
        instagramDao.addAccount(account)
    }


    suspend fun getInstagramAccount(userName: String):Response<AccountResponse>{
        return instagramSearchApi.getAccountProfile(userName)

    }

}