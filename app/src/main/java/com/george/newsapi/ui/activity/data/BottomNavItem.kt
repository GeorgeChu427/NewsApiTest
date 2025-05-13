package com.george.newsapi.ui.activity.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.george.newsapi.ui.Route

enum class BottomNavItem(
    val route: Route,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
) {

    HEADLINES(
        route = Route.HEADLINES,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,

    ),

    CONFIG(
        route = Route.CONFIG,
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
    );

}