package com.redgunner.instagramzommy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redgunner.instagramzommy.models.search.UserX
import kotlinx.coroutines.flow.Flow

@Dao
interface InstagramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: UserX)


    @Query("SELECT * FROM LocalAccountList WHERE is_favorite==1 ")
    fun getFavoriteAccounts(): Flow<List<UserX>>

    @Query("SELECT * FROM LocalAccountList")
    fun getHistoryAccounts(): Flow<List<UserX>>

}