package com.berkekucuk.mmaapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.presentation.home.HomeScreenRoot

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.Home,
        ) {
            composable<Route.Home> {
                HomeScreenRoot(
                    onFightClick = { fightId ->
                        navController.navigate(Route.FightDetail(fightId))
                    },
                    onFighterClick = { fighterId ->
                        navController.navigate(Route.FighterDetail(fighterId))
                    }
                )
            }

            composable<Route.FightDetail> { backStackEntry ->
                val route: Route.FightDetail = backStackEntry.toRoute()
            }

            composable<Route.FighterDetail> { backStackEntry ->
                val route: Route.FighterDetail = backStackEntry.toRoute()
            }
        }
    }
}
