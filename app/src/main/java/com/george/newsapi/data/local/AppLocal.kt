package com.george.newsapi.data.local

import androidx.compose.runtime.compositionLocalOf
import com.george.newsapi.data.model.store.config.ThemeMode

val LocalAppTheme = compositionLocalOf<ThemeMode> {
    error("No AppTheme provided")
}