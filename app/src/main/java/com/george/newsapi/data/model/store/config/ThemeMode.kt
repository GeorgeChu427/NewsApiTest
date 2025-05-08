package com.george.newsapi.data.model.store.config

/**
 * 主題模式
 */
enum class ThemeMode {
    LIGHT,

    DARK,

    SYSTEM;

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