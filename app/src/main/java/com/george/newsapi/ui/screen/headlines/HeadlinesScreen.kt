package com.george.newsapi.ui.screen.headlines

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.george.newsapi.ui.theme.MyApp

@Composable
fun HeadlinesScreen(
    viewModel: HeadlinesViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.getTopHeadlines()
        }
    }

    MyApp {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Text(
                text = viewState,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}