package com.george.newsapi.ui.screen.config

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.george.newsapi.data.model.store.config.LanguageCode
import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.data.repository.config.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configRepository: ConfigRepository
): ViewModel() {

    private val _languageCode = MutableStateFlow(LanguageCode.English)
    val languageCode: StateFlow<LanguageCode> = _languageCode.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    init {
        Log.i("ConfigViewModel", "[${this.hashCode().toHexString()}] init")
        viewModelScope.launch {
            launch {
                configRepository.getLanguageCode().collect {
                    _languageCode.value = it
                }
            }
            launch {
                configRepository.getThemeMode().collect {
                    _themeMode.value = it
                }
            }
        }
    }

    fun setLanguageCode(code: LanguageCode) {
        viewModelScope.launch {
            configRepository.setLanguageCode(code)
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            configRepository.setThemeMode(mode)
        }
    }
}