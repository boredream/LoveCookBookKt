package com.boredream.lovebook

import com.boredream.lovebook.di.RetrofitModule
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
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
object FakeModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        // TODO: fake api ?
        ServiceCreator.tokenFactory = { TestDataConstants.token }
        return ServiceCreator.create(ApiService::class.java)
    }

}