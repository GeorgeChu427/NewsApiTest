package com.george.newsapi.ui.fragment.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.ui.theme.MyApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigFragment: Fragment() {

    private val viewModel: ConfigViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val languageCode by viewModel.languageCode.collectAsState()
                val themeMode by viewModel.themeMode.collectAsState()
                MyApp {
                    ConfigScreen(
                        languageCode = languageCode,
                        themeMode = themeMode
                    )
                }
            }
        }
    }

    @Composable
    fun ConfigScreen(
        languageCode: String,
        themeMode: ThemeMode
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "設定")
            Spacer(modifier = Modifier.height(16.dp))

            // 語系切換
            Text(text = "語系")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = languageCode == "zh-TW",
                    onClick = {
                        viewModel.setLanguageCode("zh-TW")
                    }
                )
                Text(text = "繁體中文")
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(
                    selected = languageCode == "en",
                    onClick = { viewModel.setLanguageCode("en") }
                )
                Text(text = "English")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 主題切換
            Text(text = "主題")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = themeMode == ThemeMode.LIGHT,
                    onClick = {
                        viewModel.setThemeMode(ThemeMode.LIGHT)
                    }
                )
                Text(text = "淺色")
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(
                    selected = themeMode == ThemeMode.DARK,
                    onClick = {
                        viewModel.setThemeMode(ThemeMode.DARK)
                    }
                )
                Text(text = "深色")
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(
                    selected = themeMode == ThemeMode.SYSTEM,
                    onClick = {
                        viewModel.setThemeMode(ThemeMode.SYSTEM)
                    }
                )
                Text(text = "系統預設")
            }
        }
    }

    companion object {
        fun newInstance(): ConfigFragment {
            return ConfigFragment()
        }
    }
}