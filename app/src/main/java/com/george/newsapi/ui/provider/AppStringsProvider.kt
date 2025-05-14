package com.george.newsapi.ui.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.george.newsapi.data.model.store.config.LanguageCode
import com.george.newsapi.data.model.strings.LocalAppStrings
import com.george.newsapi.data.model.strings.chineseStrings
import com.george.newsapi.data.model.strings.englishStrings

@Composable
fun AppStringsProvider(
    languageCode: LanguageCode,
    content: @Composable () -> Unit
) {
    val strings = when (languageCode) {
        LanguageCode.Chinese -> chineseStrings
        LanguageCode.English -> englishStrings
    }

    CompositionLocalProvider(LocalAppStrings provides strings) {
        content()
    }
}