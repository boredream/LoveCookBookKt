package com.boredream.lovebook

import com.boredream.lovebook.data.repo.FakeLocationRepository
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.di.RetrofitModule
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RetrofitModule::class]
)
abstract class FakeModule {

    companion object {
        @Singleton
        @Provides
        fun provideApiService(): ApiService {
            // TODO: fake api ?
            ServiceCreator.tokenFactory = { TestDataConstants.token }
            return ServiceCreator.create(ApiService::class.java)
        }
    }

    @Singleton
    @Binds
    abstract fun provideLocationRepository(repo: FakeLocationRepository): LocationRepository

}