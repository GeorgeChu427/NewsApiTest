package com.george.newsapi.data.repository.config

import com.george.newsapi.data.model.store.config.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {

    /**
     * 取得 多國語系
     * @return Flow
     */
    fun getLanguageCode(): Flow<String>

    /**
     * 設定 多國語系
     */
    suspend fun setLanguageCode(code: String)

    /**
     * 取得 主題模式
     */
    fun getThemeMode(): Flow<ThemeMode>


    /**
     * 設定 主題模式
     */
    suspend fun setThemeMode(mode: ThemeMode)
}