package com.george.newsapi.di

import android.content.Context
import com.george.newsapi.data.store.config.AppConfigStore
import com.george.newsapi.data.store.config.AppConfigStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideAppConfigStore(
        @ApplicationContext context: Context
    ): AppConfigStore {
        return AppConfigStoreImpl(context)
    }
}