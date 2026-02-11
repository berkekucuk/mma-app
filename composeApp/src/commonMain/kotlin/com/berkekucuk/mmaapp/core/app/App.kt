package com.berkekucuk.mmaapp.core.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.presentation.screens.event_detail.EventDetailScreenRoot
import com.berkekucuk.mmaapp.presentation.screens.fight_detail.FightDetailScreenRoot

@Composable
fun App() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Route.MainGraph,
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.pagerBackground)
    ) {
        composable<Route.MainGraph>(
            enterTransition = NavTransitions.slideFromLeft,
            exitTransition = NavTransitions.slideOutToLeft,
            popEnterTransition = NavTransitions.slideFromLeft,
            popExitTransition = NavTransitions.slideOutToLeft
        ) {
            MainScreenWrapper(
                onNavigateToEventDetail = { eventId ->
                    rootNavController.navigate(Route.EventDetail(eventId))
                }
            )
        }

        composable<Route.EventDetail>(
            enterTransition = NavTransitions.slideFromRight,
            exitTransition = NavTransitions.slideOutToLeft,
            popEnterTransition = NavTransitions.slideFromLeft,
            popExitTransition = NavTransitions.slideOutToRight
        ) {
            EventDetailScreenRoot(
                onNavigateToFightDetail = { eventId, fightId ->
                    rootNavController.navigate(Route.FightDetail(eventId, fightId))
                },
                onBackClick = { rootNavController.navigateUp() }
            )
        }

        composable<Route.FightDetail>(
            enterTransition = NavTransitions.slideFromRight,
            exitTransition = NavTransitions.slideOutToLeft,
            popEnterTransition = NavTransitions.slideFromLeft,
            popExitTransition = NavTransitions.slideOutToRight
        ) {
            FightDetailScreenRoot(
                onNavigateToFighterDetail = { fighterId ->
                    rootNavController.navigate(Route.FighterDetail(fighterId))
                },
                onBackClick = { rootNavController.navigateUp() }
            )
        }


        composable<Route.FighterDetail> {
//            FighterDetailScreen(
//                fighterId = route.fighterId,
//                onBackClick = { rootNavController.navigateUp() }
//            )
        }
    }
}