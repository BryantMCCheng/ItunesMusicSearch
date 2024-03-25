package com.bryant.itunesmusicsearch.di

import com.bryant.itunesmusicsearch.data.repository.SearchRepositoryImpl
import com.bryant.itunesmusicsearch.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}