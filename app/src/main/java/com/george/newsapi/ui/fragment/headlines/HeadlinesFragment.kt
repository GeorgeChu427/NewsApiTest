package com.george.newsapi.ui.fragment.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.ui.theme.MyApp
import com.george.newsapi.ui.theme.NewsApiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlinesFragment: Fragment() {

    private val viewModel: HeadlinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApp {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Greeting(
                            text = viewModel.viewState.collectAsState().value,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getTopHeadlines()
        }
    }

    @Composable
    fun Greeting(text: String, modifier: Modifier = Modifier) {
        Text(
            text = text,
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        NewsApiTheme(themeMode = ThemeMode.LIGHT) {
            Greeting("Android")
        }
    }


    companion object {
        fun newInstance(): HeadlinesFragment {
            return HeadlinesFragment()
        }
    }
}