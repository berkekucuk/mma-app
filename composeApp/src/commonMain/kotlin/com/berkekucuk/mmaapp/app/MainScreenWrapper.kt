package com.berkekucuk.mmaapp.app

import com.berkekucuk.mmaapp.presentation.screens.home.HomeScreenRoot
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

data class TopLevelRoute<T : Route>(
    val name: String,
    val route: T,
    val icon: ImageVector
)

@Composable
fun MainScreenWrapper(
    onNavigateToEventDetail: (String) -> Unit
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        TopLevelRoute("Home", Route.Home, Icons.Filled.Home),
        TopLevelRoute("Rankings", Route.Rankings, Icons.Filled.Star),
        TopLevelRoute("Profile", Route.Profile, Icons.Filled.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.hasRoute(item.route::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.name) },
                        label = { Text(item.name) }
                    )
                }
            }
        }
    ) { bottomBarPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = Route.Home,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<Route.Home> {
                HomeScreenRoot(
                    onEventClick = onNavigateToEventDetail,
                    bottomPadding = bottomBarPadding
                    )
            }

            composable<Route.Rankings> {
                // RankingsScreen(bottomPadding = bottomBarPadding)
                Text("Rankings Screen")
            }

            composable<Route.Profile> {
                // ProfileScreen(bottomPadding = bottomBarPadding)
                Text("Profile Screen")
            }
        }
    }
}