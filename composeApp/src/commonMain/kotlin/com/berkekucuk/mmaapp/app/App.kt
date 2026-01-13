package com.berkekucuk.mmaapp.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.berkekucuk.mmaapp.presentation.screens.event_detail.EventDetailScreenRoot


@Composable
fun App() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Route.MainGraph
    ) {
        composable<Route.MainGraph> {
            MainScreenWrapper(
                onNavigateToEventDetail = { eventId ->
                    rootNavController.navigate(Route.EventDetail(eventId))
                }
            )
        }

        composable<Route.EventDetail> {
            EventDetailScreenRoot(
                onBackClick = { rootNavController.navigateUp() }
            )
        }

        composable<Route.FighterDetail> { backStackEntry ->
            val route: Route.FighterDetail = backStackEntry.toRoute()

//            FighterDetailScreen(
//                fighterId = route.fighterId,
//                onBackClick = { rootNavController.navigateUp() }
//            )
        }
    }
}