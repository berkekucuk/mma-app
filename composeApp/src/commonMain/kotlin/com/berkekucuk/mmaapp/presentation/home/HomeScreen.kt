package com.berkekucuk.mmaapp.presentation.home

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.berkekucuk.mmaapp.presentation.home.components.CompletedTab
import com.berkekucuk.mmaapp.presentation.home.components.EventsTab
import com.berkekucuk.mmaapp.presentation.home.components.HomeTopBar
import com.berkekucuk.mmaapp.presentation.theme.AppColors
import kotlinx.coroutines.launch
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
                is NavigationEvent.ToEventDetail -> onEventClick(event.eventId)
                is NavigationEvent.Back -> { /* Handle back navigation if needed */ }
            }
        }
    }

    HomeScreen(
        state = uiState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit
) {
    val tabs = listOf("Featured", "Upcoming", "Completed")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {  HomeTopBar() }
    ) { innerPadding ->

        AnimatedContent(
            targetState = state.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "HomeLoadingTransition",
            modifier = Modifier.padding(innerPadding)
        ) { isLoading ->

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // TAB ROW
                    PrimaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = AppColors.TopBarBackground,
                        contentColor = AppColors.TextPrimary,
                        indicator = {
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(pagerState.currentPage)
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(AppColors.UfcRed)
                            )
                        },
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
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

                    // PAGER CONTENT
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize().background(AppColors.PagerBackground)
                    ) { page ->
                        when (page) {
                            0 -> EventsTab(
                                events = if (state.featuredEvent != null) listOf(state.featuredEvent) else emptyList(),
                                isRefreshing = state.isRefreshingFeaturedTab,
                                onRefresh = { onAction(HomeUiAction.OnRefreshFeaturedTab) },
                                onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                                emptyMessage = "No featured event available"
                            )

                            1 -> EventsTab(
                                events = state.upcomingEvents,
                                isRefreshing = state.isRefreshingUpcomingTab,
                                onRefresh = { onAction(HomeUiAction.OnRefreshUpcomingTab) },
                                onEventClick = { onAction(HomeUiAction.OnEventClicked(it)) },
                                emptyMessage = "No upcoming events available"
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
                            )
                        }
                    }
                }
            }
        }

    }
}
