package com.george.newsapi.data.store.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.george.newsapi.data.model.store.config.LanguageCode
import com.george.newsapi.data.model.store.config.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppConfigStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppConfigStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_config")

    private object Keys {
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    override val languageCode: Flow<LanguageCode> = context.dataStore.data
        .map { prefs ->
            prefs[Keys.LANGUAGE_CODE]?.let { code ->
                LanguageCode.find(code)
            } ?: LanguageCode.English
        }

    override val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { prefs -> ThemeMode.fromString(prefs[Keys.THEME_MODE] ?: ThemeMode.SYSTEM.name) }

    override suspend fun setLanguageCode(code: LanguageCode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE_CODE] = code.ISOCode
        }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode.name
        }
    }
}