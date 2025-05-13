package com.george.newsapi.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.george.newsapi.R
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.article.Source
import com.george.newsapi.ui.theme.Purple40

/**
 * 詳細新聞頁面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: Article,
    onBackClick: (() -> Unit)? = null,
    onOpenWebClick: ((String) -> Unit)? = null
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val expandedHeight = screenHeight / 3

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Box {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = article.urlToImage,
                            contentDescription = article.title,
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.ic_launcher_foreground),
                        )
                        Row {
                            IconButton(
                                onClick = { onBackClick?.invoke() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    tint = Color.White,
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = article.title.orEmpty(),
                                color = Color.White
                            )
                        }
                    }
                },
                expandedHeight = expandedHeight,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        article.source?.let { source ->
                            SourceAuthorView(source, article.author)
                        }

                        Text(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                            text = article.content.orEmpty(),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    article.url?.let { url ->
                        onOpenWebClick?.invoke(url)
                    }
                }
            ) {
                Text(
                    text = "開啟網頁"
                )
            }
        }

    }
}

@Composable
fun CircularTextAvatar(
    name: String,
    backgroundColor: Color = Purple40
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val displayText = if (name.length >= 2) {
            name.substring(0, 2).uppercase()
        } else {
            name.uppercase()
        }
        Text(
            text = displayText,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SourceAuthorView(
    source: Source,
    author: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        source.name?.let { name ->
            CircularTextAvatar(name)
        }
        Column(
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            source.name?.let { name ->
                Text(
                    text = name,
                    fontSize = 24.sp
                )
            }
            author?.let {
                Text(
                    text = author,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val previewArticle = Article(
        source = Source(id = "source-id", name = "SourceName"),
        author = "George",
        title = "文章標題，文章標題，文章標題，文章標題，文章標題，文章標題，文章標題，文章標題，",
        description = "文章描述，文章描述，文章描述，文章描述，文章描述，文章描述，文章描述，文章描述，\n文章描述，文章描述，文章描述，文章描述，文章描述。",
        url = "https://example.com",
        urlToImage = "https://picsum.photos/800/400",
        publishedAt = "2024-03-20 10:10:10",
        content = "文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。文章內容。"
    )

    MaterialTheme {
        DetailScreen(
            article = previewArticle,
            onBackClick = {}
        )
    }
}