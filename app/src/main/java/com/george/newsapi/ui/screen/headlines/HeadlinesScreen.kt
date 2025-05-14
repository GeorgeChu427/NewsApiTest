package com.george.newsapi.ui.screen.headlines

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.george.newsapi.data.model.api.article.ArticleCategory
import com.george.newsapi.data.model.api.article.displayName
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.SharedArticleViewModel
import com.george.newsapi.ui.theme.MyApp
import com.george.newsapi.ui.theme.Purple40

@Composable
fun HeadlinesScreen(
    mainNavController: NavController,
    sharedViewModel: SharedArticleViewModel,
    viewModel: HeadlinesViewModel = hiltViewModel()
) {
    val articles = viewModel.topHeadlinesPagingFlow.collectAsLazyPagingItems()
    val currentCategory by viewModel.category.collectAsStateWithLifecycle()

    MyApp {
        Column {
            CategoryTabRow(
                selectedCategory = currentCategory,
                onCategorySelected = {
                    viewModel.setCategory(it)
                }
            )

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
                                        Route.DETAIL.navigate(mainNavController)
                                    }
                            )
                        }
                    }

                    // paging 加載狀態處理
                    articles.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
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
}

@Composable
fun CategoryTabRow(
    selectedCategory: ArticleCategory,
    onCategorySelected: (ArticleCategory) -> Unit
) {
    val categories = ArticleCategory.entries
    val selectedIndex = ArticleCategory.entries.indexOf(selectedCategory).takeIf { it >= 0 } ?: 0

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth(),
        edgePadding = 16.dp,
        indicator = {},
        divider = {}
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = {
                    onCategorySelected(category)
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == selectedIndex) Purple40 else Color.LightGray
                    ),
                selectedContentColor = Color.White,
                unselectedContentColor = Color.DarkGray,
                text = {
                    Text(
                        text = category.displayName(),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            )
        }
    }
}