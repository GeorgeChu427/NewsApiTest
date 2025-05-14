package com.george.newsapi.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.george.newsapi.SCREEN_NAV_DURATION

enum class Route(
    val route: String,
    val title: String,
    val isHomeScreen: Boolean = false,
    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null
) {

    /**
     * 首頁
     */
    MAIN(
        route = "main",
        title = "Main"
    ),

    /**
     * 頭條新聞
     */
    HEADLINES(
        route = "headlines",
        title = "Headlines",
        isHomeScreen = true
    ),

    /**
     * APP 設定頁
     */
    CONFIG(
        route = "config",
        title = "Setting",
        isHomeScreen = true
    ),

    /**
     * 詳細新聞頁面
     */
    DETAIL(
        route = "detail",
        title = "News",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(SCREEN_NAV_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(SCREEN_NAV_DURATION)
            )
        }
    ),

    /**
     * WebView
     */
    WEB(
        route = "webView",
        title = "Web",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(SCREEN_NAV_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(SCREEN_NAV_DURATION)
            )
        }
    ),

    /**
     * 搜尋頁
     */
    SEARCH(
        route = "search",
        title = "Search",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(SCREEN_NAV_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(SCREEN_NAV_DURATION)
            )
        }
    );

    fun navigate(navController: NavController) {
        navController.navigate(route)
    }


    companion object {

        fun find(route: String): Route {
            return entries.find {
                it.route == route
            } ?: HEADLINES
        }

    }
}

fun NavGraphBuilder.composableByRoute(
    routeEnum: Route,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = this.composable(
    route = routeEnum.route,
    enterTransition = routeEnum.enterTransition,
    exitTransition = routeEnum.exitTransition,
    content = content
)