package com.boredream.lovebook.di

import com.boredream.lovebook.data.repo.FakeLocationRepository
import com.boredream.lovebook.data.repo.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun provideLocationRepository(repo: FakeLocationRepository): LocationRepository

}