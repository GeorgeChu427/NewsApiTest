package com.george.newsapi.data.model.store.config

import com.george.newsapi.data.model.strings.AppStrings

/**
 * 主題模式
 */
enum class ThemeMode {
    LIGHT,

    DARK,

    SYSTEM;

    fun getDisplayName(appStrings: AppStrings) : String {
        return when (this) {
            LIGHT -> appStrings.themeLight
            DARK -> appStrings.themeDark
            SYSTEM -> appStrings.themeSystem
        }
    }

    companion object {
        fun fromString(value: String): ThemeMode {
            return try {
                valueOf(value)
            } catch (_: Exception) {
                SYSTEM
            }
        }
    }
}