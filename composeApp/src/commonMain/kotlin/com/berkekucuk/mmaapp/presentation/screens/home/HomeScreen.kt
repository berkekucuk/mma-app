package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onEventClick: (String) -> Unit,
    bottomPadding: PaddingValues,
    ) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is NavigationEvent.ToEventDetail -> onEventClick(event.eventId)
                is NavigationEvent.Back -> { }
            }
        }
    }

    HomeScreen(
        state = uiState,
        onAction = viewModel::onAction,
        bottomPadding = bottomPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    bottomPadding: PaddingValues
) {
    val tabs = listOf("Featured", "Upcoming", "Completed")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val featuredListState = rememberLazyListState()
    val upcomingListState = rememberLazyListState()
    val completedListState = rememberLazyListState()

    Scaffold(
        containerColor = AppColors.pagerBackground,
        topBar = { HomeTopBar() }
    ) { topBarPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.pagerBackground)
                .padding(top = topBarPadding.calculateTopPadding())
        ) {

            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = AppColors.topBarBackground,
                contentColor = AppColors.textPrimary,
                indicator = {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(pagerState.currentPage)
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(AppColors.ufcRed)
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    )
                }
            }

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

                        val sharedContentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = bottomPadding.calculateBottomPadding() + 16.dp
                        )

                        when (page) {
                            0 -> EventsTab(
                                events = if (state.featuredEvent != null) listOf(state.featuredEvent) else emptyList(),
                                isRefreshing = state.isRefreshingFeaturedTab,
                                onRefresh = { onAction(HomeUiAction.OnRefreshFeaturedTab) },
                                onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                                emptyMessage = "No featured event available",
                                listState = featuredListState,
                                contentPadding = sharedContentPadding
                            )

                            1 -> EventsTab(
                                events = state.upcomingEvents,
                                isRefreshing = state.isRefreshingUpcomingTab,
                                onRefresh = { onAction(HomeUiAction.OnRefreshUpcomingTab) },
                                onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                                emptyMessage = "No upcoming events available",
                                listState = upcomingListState,
                                contentPadding = sharedContentPadding
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
                                contentPadding = sharedContentPadding
                            )
                        }
                    }
                }
            }
        }
    }
}
