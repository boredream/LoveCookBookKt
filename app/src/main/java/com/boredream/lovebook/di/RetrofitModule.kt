package com.boredream.lovebook.di

import com.boredream.lovebook.data.constant.DataStoreKey
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
import com.boredream.lovebook.utils.DataStoreUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        ServiceCreator.tokenFactory = { DataStoreUtils.readStringData(DataStoreKey.TOKEN, "") }
        return ServiceCreator.create(ApiService::class.java)
    }

}