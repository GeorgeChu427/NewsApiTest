package com.george.newsapi.data.store.config

import com.george.newsapi.data.model.store.config.ThemeMode
import kotlinx.coroutines.flow.Flow

/**
 * 簡易的 App 設定
 */
interface AppConfigStore {

    /**
     * 取得 多國語系
     */
    val languageCode: Flow<String>

    /**
     * 設定 多國語系
     */
    suspend fun setLanguageCode(code: String)

    /**
     * 取得 主題模式
     */
    val themeMode: Flow<ThemeMode>


    /**
     * 設定 主題模式
     */
    suspend fun setThemeMode(mode: ThemeMode)

}