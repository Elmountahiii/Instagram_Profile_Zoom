package com.redgunner.instagramzommy.database.builder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redgunner.instagramzommy.database.dao.InstagramDao
import com.redgunner.instagramzommy.models.search.UserX

@Database(entities = [UserX::class], version = 1)
abstract class LocalAccountDatabaseBuilder : RoomDatabase() {


    abstract fun instagramDao(): InstagramDao


    companion object {
        private var Instance: LocalAccountDatabaseBuilder? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = Instance
            ?: synchronized(LOCK) {

                Instance
                    ?: buildDatabase(
                        context
                    )
                        .also { Instance = it }


            }







        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            LocalAccountDatabaseBuilder::class.java, "LocalAccount.db"
        ).fallbackToDestructiveMigration()
            .build()






    }


}