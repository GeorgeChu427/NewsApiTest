package com.george.newsapi.ui.fragment.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.data.repository.config.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configRepository: ConfigRepository
): ViewModel() {

    private val _languageCode = MutableStateFlow("")
    val languageCode: StateFlow<String> = _languageCode.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    init {
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

    fun setLanguageCode(code: String) {
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