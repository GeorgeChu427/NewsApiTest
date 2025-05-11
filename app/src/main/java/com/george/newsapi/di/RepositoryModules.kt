package com.george.newsapi.di

import com.george.newsapi.data.repository.ArticleRepository
import com.george.newsapi.data.repository.ArticleRepositoryImpl
import com.george.newsapi.data.repository.config.ConfigRepository
import com.george.newsapi.data.repository.config.ConfigRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModules {

    @Binds
    abstract fun bindArticleRepository(
        impl: ArticleRepositoryImpl
    ): ArticleRepository

    @Binds
    abstract fun bindConfigRepository(
        impl: ConfigRepositoryImpl
    ): ConfigRepository
}