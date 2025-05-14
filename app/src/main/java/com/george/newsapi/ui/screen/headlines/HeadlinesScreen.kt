package com.george.newsapi.ui.screen.headlines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.SharedArticleViewModel
import com.george.newsapi.ui.theme.MyApp

@Composable
fun HeadlinesScreen(
    navController: NavController,
    viewModel: HeadlinesViewModel = hiltViewModel(),
    sharedViewModel: SharedArticleViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val articles = viewModel.topHeadlinesPagingFlow.collectAsLazyPagingItems()
    val currentCategory by viewModel.category.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.randomCategory()
        }
    }

    MyApp {
        key(currentCategory) {
            LazyColumn {
                items(articles.itemCount) { index ->
                    val article = articles[index]
                    if (article != null) {
                        ArticleCard(
                            article = article,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    sharedViewModel.selectArticle(article)
                                    Route.DETAIL.navigate(navController)
                                }
                        )
                    }
                }

                // paging 加載狀態處理
                articles.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { CircularProgressIndicator(Modifier.padding(16.dp)) }
                        }

                        loadState.append is LoadState.Loading -> {
                            item { CircularProgressIndicator(Modifier.padding(16.dp)) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val e = loadState.refresh as LoadState.Error
                            item {
                                Text("Error: ${e.error.message}", color = Color.Red)
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = loadState.append as LoadState.Error
                            item {
                                Text("More Error: ${e.error.message}", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}