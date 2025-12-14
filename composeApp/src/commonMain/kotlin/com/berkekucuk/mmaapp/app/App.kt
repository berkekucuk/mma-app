package com.berkekucuk.mmaapp.app

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
                    onEventClick = { eventId ->
                        navController.navigate(Route.EventDetail(eventId))
                    }
                )
            }

            composable<Route.EventDetail> { backStackEntry ->
                val route: Route.EventDetail = backStackEntry.toRoute()
                // TODO: EventDetailScreen(eventId = route.eventId)
            }

            composable<Route.FightDetail> { backStackEntry ->
                val route: Route.FightDetail = backStackEntry.toRoute()
                // TODO: FightDetailScreen(fightId = route.fightId)
            }

            composable<Route.FighterDetail> { backStackEntry ->
                val route: Route.FighterDetail = backStackEntry.toRoute()
                // TODO: FighterDetailScreen(fighterId = route.fighterId)
            }
        }
    }
}
