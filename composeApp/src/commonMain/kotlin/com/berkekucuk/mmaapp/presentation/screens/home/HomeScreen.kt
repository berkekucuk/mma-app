package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import kotlinx.coroutines.launch
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.events_title
import mmaapp.composeapp.generated.resources.tab_completed
import mmaapp.composeapp.generated.resources.tab_upcoming
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToEventDetail: (String) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is HomeNavigationEvent.ToEventDetail -> onNavigateToEventDetail(event.eventId)
            }
        }
    }

    HomeScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    val coroutineScope = rememberCoroutineScope()

    val onRefreshUpcomingTab = remember(onAction) { { onAction(HomeUiAction.OnRefreshUpcomingTab) } }
    val onRefreshCompletedTab = remember(onAction) { { onAction(HomeUiAction.OnRefreshCompletedTab) } }
    val onEventClicked = remember(onAction) { { eventId: String -> onAction(HomeUiAction.OnEventClicked(eventId)) } }
    val onYearSelected = remember(onAction) { { year: Int -> onAction(HomeUiAction.OnYearSelected(year)) } }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.topBarBackground)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.events_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = AppFonts.RobotoCondensed,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColors.topBarBackground,
                        scrolledContainerColor = AppColors.topBarBackground,
                        titleContentColor = AppColors.textPrimary,
                    ),
                    scrollBehavior = scrollBehavior
                )

                AppTabRow(
                    tabs = tabs,
                    pagerState = pagerState,
                    coroutineScope = coroutineScope
                )
            }
        }
    ) { innerPadding ->
        LoadingContent(
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.pagerBackground),
                beyondViewportPageCount = 1
            ) { page ->
                when (page) {
                    0 -> UpcomingContainer(
                        events = state.upcomingEvents,
                        isRefreshing = state.isRefreshingUpcomingTab,
                        onRefresh = onRefreshUpcomingTab,
                        onEventClick = onEventClicked,
                        listState = upcomingListState,
                    )
                    1 -> CompletedContainer(
                        completedEvents = state.completedEvents,
                        isRefreshing = state.isRefreshingCompletedTab,
                        onRefresh = onRefreshCompletedTab,
                        onEventClick = onEventClicked,
                        availableYears = state.availableYears,
                        selectedYear = state.selectedYear,
                        isYearLoading = state.isYearLoading,
                        onYearSelected = onYearSelected,
                        listState = completedListState,
                    )
                }
            }
        }
    }
}
