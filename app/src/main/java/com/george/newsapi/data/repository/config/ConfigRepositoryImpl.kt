package com.george.newsapi.data.repository.config

import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.data.store.config.AppConfigStore
import com.george.newsapi.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val configDataStore: AppConfigStore,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ConfigRepository {

    override fun getLanguageCode(): Flow<String> {
        return configDataStore.languageCode
            .flowOn(dispatcher)
    }

    override suspend fun setLanguageCode(code: String) {
        configDataStore.setLanguageCode(code)
    }

    override fun getThemeMode(): Flow<ThemeMode> {
        return configDataStore.themeMode
            .flowOn(dispatcher)
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        configDataStore.setThemeMode(mode)
    }


}