package com.george.newsapi.ui.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.SharedArticleViewModel
import com.george.newsapi.ui.screen.headlines.ArticleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    mainNavController: NavController,
    sharedViewModel: SharedArticleViewModel,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val articles = searchViewModel.searchPagingFlow.collectAsLazyPagingItems()
    val query by searchViewModel.input.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SearchTopAppBar(
                modifier = Modifier,
                query = searchQuery,
                onQueryChanged = {
                    searchQuery = it
                },
                onSearchConfirmed = {
                    searchViewModel.setInput(it)
                },
                onBackClick = {
                    mainNavController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        key(query) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    modifier: Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchConfirmed: (String) -> Unit,
    onBackClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = { newText ->
                    onQueryChanged.invoke(newText)
                },
                label = { Text("請輸入關鍵字") }
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onSearchConfirmed.invoke(query)
                }
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Confirm"
                )
            }
        }
    )
}