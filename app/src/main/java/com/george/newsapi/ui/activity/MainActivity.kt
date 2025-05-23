package com.george.newsapi.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.ColorInt
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.george.newsapi.R
import com.george.newsapi.data.model.store.config.ThemeMode
import com.george.newsapi.data.model.strings.Strings
import com.george.newsapi.ext.context.isSystemInDarkTheme
import com.george.newsapi.ui.Route
import com.george.newsapi.ui.activity.data.BottomNavItem
import com.george.newsapi.ui.composableByRoute
import com.george.newsapi.ui.dialog.SwitchLanguageDialog
import com.george.newsapi.ui.provider.AppStringsProvider
import com.george.newsapi.ui.screen.config.ConfigScreen
import com.george.newsapi.ui.screen.config.ConfigViewModel
import com.george.newsapi.ui.screen.detail.DetailScreen
import com.george.newsapi.ui.screen.headlines.HeadlinesScreen
import com.george.newsapi.ui.screen.search.SearchScreen
import com.george.newsapi.ui.screen.webview.WebViewScreen
import com.george.newsapi.ui.theme.MyApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedArticleViewModel: SharedArticleViewModel by viewModels()
    private val configViewModel: ConfigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp(configViewModel) {
                val mainNavController = rememberNavController()

                val languageCode by configViewModel.languageCode.collectAsStateWithLifecycle()
                var isShowSwitchLanguageDialog by remember { mutableStateOf(false) }

                AppStringsProvider(languageCode = languageCode) {
                    NavHost(
                        navController = mainNavController,
                        startDestination = Route.MAIN.route,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composableByRoute(Route.MAIN) {
                            MainScreen(
                                mainNavController,
                                sharedArticleViewModel,
                                onClickSwitchLanguage = {
                                    isShowSwitchLanguageDialog = true
                                }
                            )
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

                    if (isShowSwitchLanguageDialog) {
                        SwitchLanguageDialog(
                            currentSelection = languageCode,
                            onDismiss = {
                                isShowSwitchLanguageDialog = false
                            },
                            onConfirm = { newCode ->
                                configViewModel.setLanguageCode(newCode)
                                isShowSwitchLanguageDialog = false
                            }
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            configViewModel.themeMode
                .flowWithLifecycle(lifecycle)
                .collect {
                    setStatusBarColor(it)
                }
        }
    }

    @SuppressLint("ContextCastToActivity")
    @Composable
    fun MainScreen(
        mainNavController: NavHostController,
        sharedArticleViewModel: SharedArticleViewModel,
        onClickSwitchLanguage: () -> Unit
    ) {
        val navController = rememberNavController()

        var selectedItemIndex: Int by remember {
            mutableIntStateOf(0)
        }

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
                        text = currentRoute.getTitle(Strings.current),
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
                                onClickSwitchLanguage.invoke()
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
                                    Text(text = item.route.getTitle(Strings.current))
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
                    ConfigScreen(
                        viewModel = configViewModel
                    )
                }
            }
        }
    }

    fun setStatusBarColor(themeMode: ThemeMode) {
        val isLightMode = when (themeMode) {
            ThemeMode.LIGHT -> true
            ThemeMode.DARK -> false
            ThemeMode.SYSTEM -> !isSystemInDarkTheme()
        }

        window.statusBarColor = if (isLightMode) {
            Color.White.toArgb()
        } else {
            Color.Black.toArgb()
        }

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.insetsController?.setSystemBarsAppearance(
                    if (isLightMode) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val decorView = window.decorView
                decorView.systemUiVisibility = if (isLightMode) {
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }
}

