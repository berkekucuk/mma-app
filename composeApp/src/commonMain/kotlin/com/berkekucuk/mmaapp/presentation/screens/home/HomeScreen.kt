package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.tab_completed
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
        stringResource(Res.string.tab_upcoming),
        stringResource(Res.string.tab_completed)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val upcomingListState = rememberLazyListState()
    val completedListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.pagerBackground)
    ) {
        HomeTopBar(pagerState = pagerState, tabs = tabs)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AppColors.pagerBackground),
            beyondViewportPageCount = 1
        ) { page ->

            when (page) {
                0 -> UpcomingTab(
                    events = state.upcomingEvents,
                    isRefreshing = state.isRefreshingUpcomingTab,
                    onRefresh = { onAction(HomeUiAction.OnRefreshUpcomingTab) },
                    onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                    listState = upcomingListState,
                )

                1 -> CompletedTab(
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
