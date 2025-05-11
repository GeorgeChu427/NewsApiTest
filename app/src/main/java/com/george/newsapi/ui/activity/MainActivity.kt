package com.george.newsapi.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.george.newsapi.ui.activity.data.BottomNavItem
import com.george.newsapi.ui.screen.config.ConfigScreen
import com.george.newsapi.ui.screen.headlines.HeadlinesScreen
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

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        var selectedItemIndex: Int by rememberSaveable {
            mutableIntStateOf(0)
        }

        Scaffold(
            bottomBar = {
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
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.HEADLINES.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(BottomNavItem.HEADLINES.route) { HeadlinesScreen() }
                composable(BottomNavItem.CONFIG.route) { ConfigScreen() }
            }
        }
    }
}

