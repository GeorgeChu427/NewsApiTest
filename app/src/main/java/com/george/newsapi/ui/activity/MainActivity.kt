package com.george.newsapi.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.george.newsapi.R
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.data.BottomNavItem
import com.george.newsapi.ui.composableByRoute
import com.george.newsapi.ui.screen.config.ConfigScreen
import com.george.newsapi.ui.screen.detail.DetailScreen
import com.george.newsapi.ui.screen.headlines.HeadlinesScreen
import com.george.newsapi.ui.screen.search.SearchScreen
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
                val mainNavController = rememberNavController()

                val sharedArticleViewModel: SharedArticleViewModel = hiltViewModel()

                NavHost(
                    navController = mainNavController,
                    startDestination = Route.MAIN.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composableByRoute(Route.MAIN) {
                        MainScreen(mainNavController, sharedArticleViewModel)
                    }
                    composableByRoute(Route.DETAIL) {
                        val article by sharedArticleViewModel.selectedArticle.collectAsState()

                        if (article != null) {
                            DetailScreen(
                                article = article!!,
                                onBackClick = {
                                    mainNavController.popBackStack()
                                    sharedArticleViewModel.clear()
                                },
                                onOpenWebClick = { url ->
                                    Route.WEB.navigate(mainNavController)
                                }
                            )
                        } else {
                            Text("Article not found")
                        }
                    }

                    composableByRoute(Route.WEB) {
                        val article by sharedArticleViewModel.selectedArticle.collectAsState()
                        if (article != null && !article?.url.isNullOrBlank()) {
                            WebViewScreen(
                                article = article!!,
                                onBackClick = {
                                    mainNavController.popBackStack()
                                }
                            )
                        }
                    }

                    composableByRoute(Route.SEARCH) {
                        SearchScreen(
                            mainNavController = mainNavController,
                            sharedViewModel = sharedArticleViewModel
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("ContextCastToActivity")
    @Composable
    fun MainScreen(
        mainNavController: NavHostController,
        sharedArticleViewModel: SharedArticleViewModel
    ) {
        val navController = rememberNavController()

        var selectedItemIndex: Int by rememberSaveable {
            mutableIntStateOf(0)
        }

        // 控制 NavigationBar 顯示與否
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route?.let {
            Route.find(it)
        } ?: Route.HEADLINES

        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .padding(WindowInsets.statusBars.asPaddingValues())
                        .height(50.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable {
                                Route.SEARCH.navigate(mainNavController)
                            }
                    )
                    Text(
                        text = currentRoute.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_switch),
                        contentDescription = "Switch Language",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                // todo Switch Language
                            }
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(visible = currentRoute.isHomeScreen) {
                    NavigationBar {
                        BottomNavItem.entries.forEachIndexed { index, item ->
                            val isSelected = selectedItemIndex == index
                            NavigationBarItem(
                                selected = isSelected,
                                label = {
                                    Text(text = item.route.title)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                                        contentDescription = item.route.title
                                    )
                                },
                                onClick = {
                                    selectedItemIndex = index
                                    item.route.navigate(navController)
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Route.HEADLINES.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composableByRoute(Route.HEADLINES) {
                    HeadlinesScreen(
                        mainNavController = mainNavController,
                        sharedViewModel = sharedArticleViewModel
                    )
                }
                composableByRoute(Route.CONFIG) {
                    ConfigScreen()
                }
            }
        }
    }
}

