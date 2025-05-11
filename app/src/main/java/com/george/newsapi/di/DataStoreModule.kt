package com.george.newsapi.di

import com.george.newsapi.data.store.config.AppConfigStore
import com.george.newsapi.data.store.config.AppConfigStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindAppConfigStore(
        impl: AppConfigStoreImpl
    ): AppConfigStore

}