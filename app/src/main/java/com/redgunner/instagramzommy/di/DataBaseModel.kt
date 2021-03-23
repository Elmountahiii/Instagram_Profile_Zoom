package com.redgunner.instagramzommy.di

import android.content.Context
import com.redgunner.instagramzommy.database.builder.LocalAccountDatabaseBuilder
import com.redgunner.instagramzommy.database.dao.InstagramDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DataBaseModel {



    @Singleton
    @Provides
    fun  provideLocalDatabase(@ApplicationContext context: Context):LocalAccountDatabaseBuilder{
        return LocalAccountDatabaseBuilder.invoke(context)
    }



    @Singleton
    @Provides
    fun provideInstagramDao(localAccountDatabaseBuilder: LocalAccountDatabaseBuilder):InstagramDao{
        return localAccountDatabaseBuilder.instagramDao()
    }


}