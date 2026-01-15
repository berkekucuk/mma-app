package com.berkekucuk.mmaapp.app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
        composable<Route.MainGraph>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            }
        ) {
            MainScreenWrapper(
                onNavigateToEventDetail = { eventId ->
                    rootNavController.navigate(Route.EventDetail(eventId))
                }
            )
        }

        composable<Route.EventDetail>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            EventDetailScreenRoot(
                onNavigateToFightDetail = { fightId ->
                    rootNavController.navigate(Route.FightDetail(fightId))
                },
                onBackClick = { rootNavController.navigateUp() }
            )
        }


        composable<Route.FightDetail>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            val route: Route.FightDetail = backStackEntry.toRoute()
            // TODO: FightDetailScreenRoot(
            //     fightId = route.fightId,
            //     onBackClick = { rootNavController.navigateUp() }
            // )
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
