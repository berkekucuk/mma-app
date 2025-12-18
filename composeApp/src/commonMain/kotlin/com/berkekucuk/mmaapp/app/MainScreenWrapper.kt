package com.berkekucuk.mmaapp.app

import androidx.compose.foundation.layout.Box
import com.berkekucuk.mmaapp.presentation.screens.home.HomeScreenRoot
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.berkekucuk.mmaapp.core.presentation.AppColors

@Composable
fun MainScreenWrapper(
    onNavigateToEventDetail: (String) -> Unit
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = AppColors.topBarBackground,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination,
                onNavigate = { item ->
                    bottomNavController.navigate(item.route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { bottomBarPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottomBarPadding),
            color = AppColors.pagerBackground
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Route.Home
            ) {
                composable<Route.Home> {
                    HomeScreenRoot(
                        onEventClick = onNavigateToEventDetail,
                    )
                }

                composable<Route.Rankings> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Rankings Screen", color = Color.White)
                    }
                }

                composable<Route.Profile> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Profile Screen", color = Color.White)
                    }
                }
            }
        }
    }
}