package com.george.newsapi.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.data.BottomNavItem
import com.george.newsapi.ui.screen.config.ConfigScreen
import com.george.newsapi.ui.screen.detail.DetailScreen
import com.george.newsapi.ui.screen.headlines.HeadlinesScreen
import com.george.newsapi.ui.screen.webview.WebViewScreen
import com.george.newsapi.ui.theme.MyApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                MainScreen()
            }
        }
    }

    @SuppressLint("ContextCastToActivity")
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        var selectedItemIndex: Int by rememberSaveable {
            mutableIntStateOf(0)
        }

        val sharedArticleViewModel: SharedArticleViewModel =
            hiltViewModel(LocalContext.current as ComponentActivity)

        // 控制 NavigationBar 顯示與否
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val showBottomBar = when (currentRoute) {
            Route.HEADLINES, Route.CONFIG -> true
            else -> false
        }

        Scaffold(
            bottomBar = {
                AnimatedVisibility(visible = showBottomBar) {
                    NavigationBar {
                        BottomNavItem.entries.forEachIndexed { index, item ->
                            val isSelected = selectedItemIndex == index
                            NavigationBarItem(
                                selected = isSelected,
                                label = {
                                    Text(text = item.title)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                                        contentDescription = item.title
                                    )
                                },
                                onClick = {
                                    selectedItemIndex = index
                                    navController.navigate(item.route)
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Route.HEADLINES,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Route.HEADLINES) {
                    HeadlinesScreen(
                        navController,
                        sharedViewModel = sharedArticleViewModel
                    )
                }
                composable(Route.CONFIG) { ConfigScreen() }

                composable(Route.DETAIL) {
                    val article by sharedArticleViewModel.selectedArticle.collectAsState()

                    if (article != null) {
                        DetailScreen(
                            article = article!!,
                            onBackClick = {
                                navController.popBackStack()
                                sharedArticleViewModel.clear()
                            },
                            onOpenWebClick = { url ->
                                navController.navigate(Route.WEB)
                            }
                        )
                    } else {
                        Text("Article not found")
                    }
                }

                composable(Route.WEB) {
                    val article by sharedArticleViewModel.selectedArticle.collectAsState()

                    if (article != null && !article?.url.isNullOrBlank()) {
                        WebViewScreen(
                            article = article!!,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

