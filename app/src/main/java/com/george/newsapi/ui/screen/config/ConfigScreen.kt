package com.george.newsapi.ui.screen.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.george.newsapi.data.model.store.config.LanguageCode
import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.data.model.strings.Strings

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = hiltViewModel()
) {
    val languageCode by viewModel.languageCode.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 語系切換
        Text(text = Strings.current.language)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageCode.entries.forEach { language ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .weight(1f)
                ) {
                    RadioButton(
                        selected = (languageCode == language),
                        onClick = {
                            viewModel.setLanguageCode(language)
                        }
                    )
                    Text(text = language.displayName)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 主題切換
        Text(text = Strings.current.theme)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = themeMode == mode,
                        onClick = {
                            viewModel.setThemeMode(mode)
                        }
                    )
                    Text(text = mode.getDisplayName(Strings.current))
                }
            }
        }
    }
}