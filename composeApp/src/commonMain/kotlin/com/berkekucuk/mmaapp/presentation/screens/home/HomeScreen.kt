package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.empty_featured_events
import mmaapp.composeapp.generated.resources.empty_upcoming_events
import mmaapp.composeapp.generated.resources.tab_completed
import mmaapp.composeapp.generated.resources.tab_featured
import mmaapp.composeapp.generated.resources.tab_upcoming
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onEventClick: (String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is HomeNavigationEvent.ToEventDetail -> onEventClick(event.eventId)
            }
        }
    }

    HomeScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
) {
    val tabs = listOf(
        stringResource(Res.string.tab_featured),
        stringResource(Res.string.tab_upcoming),
        stringResource(Res.string.tab_completed)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val featuredListState = rememberLazyListState()
    val upcomingListState = rememberLazyListState()
    val completedListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.pagerBackground)
    ) {
        HomeTopBar(pagerState = pagerState, tabs = tabs)

        AnimatedContent(
            targetState = state.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "HomeContentTransition",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { isLoading ->

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.pagerBackground),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.ufcRed)
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.pagerBackground),
                    beyondViewportPageCount = 1
                ) { page ->

                    when (page) {
                        0 -> EventsTab(
                            events = if (state.featuredEvent != null) listOf(state.featuredEvent) else emptyList(),
                            isRefreshing = state.isRefreshingFeaturedTab,
                            onRefresh = { onAction(HomeUiAction.OnRefreshFeaturedTab) },
                            onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                            emptyMessage = stringResource(Res.string.empty_featured_events),
                            listState = featuredListState,
                        )

                        1 -> EventsTab(
                            events = state.upcomingEvents,
                            isRefreshing = state.isRefreshingUpcomingTab,
                            onRefresh = { onAction(HomeUiAction.OnRefreshUpcomingTab) },
                            onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                            emptyMessage = stringResource(Res.string.empty_upcoming_events),
                            listState = upcomingListState,
                        )

                        2 -> CompletedTab(
                            completedEvents = state.completedEvents,
                            isRefreshing = state.isRefreshingCompletedTab,
                            onRefresh = { onAction(HomeUiAction.OnRefreshCompletedTab) },
                            onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                            availableYears = state.availableYears,
                            selectedYear = state.selectedYear,
                            isYearLoading = state.isYearLoading,
                            onYearSelected = { onAction(HomeUiAction.OnYearSelected(it)) },
                            listState = completedListState,
                        )
                    }
                }
            }
        }
    }
}
