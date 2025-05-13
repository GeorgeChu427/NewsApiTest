package com.george.newsapi.ui.screen.headlines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.SharedArticleViewModel
import com.george.newsapi.ui.theme.MyApp
import kotlin.random.Random

@Composable
fun HeadlinesScreen(
    navController: NavController,
    viewModel: HeadlinesViewModel = hiltViewModel(),
    sharedViewModel: SharedArticleViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val articles by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.getTopHeadlines()
        }
    }

    MyApp {
        LazyColumn {
            itemsIndexed(
                items = articles,
                key = { _, item ->
                    item.title ?: Random.nextInt(0, Int.MAX_VALUE).toString()
                }
            ) { _, item ->
                ArticleCard(
                    article = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            sharedViewModel.selectArticle(item)
                            Route.DETAIL.navigate(navController)
                        }
                )
            }
        }
    }
}