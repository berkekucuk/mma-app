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
import com.berkekucuk.mmaapp.presentation.screens.fighter_detail.FighterDetailScreenRoot
import com.berkekucuk.mmaapp.presentation.screens.profile.edit.ProfileEditScreenRoot
import com.berkekucuk.mmaapp.presentation.screens.ranking_detail.RankingDetailScreenRoot

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
                },
                onNavigateToFighterDetail = { fighterId ->
                    rootNavController.navigate(Route.FighterDetail(fighterId))
                },
                onNavigateToProfileEdit = {
                    rootNavController.navigate(Route.ProfileEdit)
                onNavigateToRankingDetail = { weightClassId, weightClassName ->
                    rootNavController.navigate(Route.RankingDetail(weightClassId, weightClassName))
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

        composable<Route.FighterDetail>(
            enterTransition = NavTransitions.slideFromRight,
            exitTransition = NavTransitions.slideOutToLeft,
            popEnterTransition = NavTransitions.slideFromLeft,
            popExitTransition = NavTransitions.slideOutToRight
        ) {
            FighterDetailScreenRoot(
                onNavigateToFightDetail = { eventId, fightId, fighterId ->
                    rootNavController.navigate(Route.FightDetail(eventId, fightId, fighterId))
                },
                onBackClick = { rootNavController.navigateUp() }
            )
        }

         composable<Route.ProfileEdit>(
            enterTransition = NavTransitions.slideFromRight,
            exitTransition = NavTransitions.slideOutToLeft,
            popEnterTransition = NavTransitions.slideFromLeft,
            popExitTransition = NavTransitions.slideOutToRight
        ) {
            ProfileEditScreenRoot(
                onBackClick = { rootNavController.navigateUp() }
            )
        }

        composable<Route.RankingDetail>(
            enterTransition = NavTransitions.slideFromRight,
            exitTransition = NavTransitions.slideOutToLeft,
            popEnterTransition = NavTransitions.slideFromLeft,
            popExitTransition = NavTransitions.slideOutToRight
        ) {
            RankingDetailScreenRoot(
                onNavigateBack = { rootNavController.navigateUp() },
                onNavigateToFighterDetail = { fighterId ->
                    rootNavController.navigate(Route.FighterDetail(fighterId))
                }
            )
        }
    }
}
