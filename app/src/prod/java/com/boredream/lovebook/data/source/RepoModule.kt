package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.repo.source.GdLocationDataSource
import com.boredream.lovebook.data.repo.source.LocationDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideLocationDataSource(repo: GdLocationDataSource): LocationDataSource

}