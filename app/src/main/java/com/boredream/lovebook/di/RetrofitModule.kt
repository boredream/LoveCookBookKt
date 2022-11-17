package com.boredream.lovebook.di

import com.boredream.lovebook.data.constant.DataStoreKey
import com.boredream.lovebook.data.repo.DefaultLocationRepository
import com.boredream.lovebook.data.repo.FakeLocationRepository
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
import com.boredream.lovebook.utils.DataStoreUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule {

    companion object {
        @Singleton
        @Provides
        fun provideApiService(): ApiService {
            ServiceCreator.tokenFactory = { DataStoreUtils.readStringData(DataStoreKey.TOKEN, "") }
            return ServiceCreator.create(ApiService::class.java)
        }
    }

    @Singleton
    @Binds
    abstract fun provideLocationRepository(repo: FakeLocationRepository): LocationRepository

}