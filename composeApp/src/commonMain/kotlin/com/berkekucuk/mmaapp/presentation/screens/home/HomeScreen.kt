package com.berkekucuk.mmaapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.AppErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbarEffect
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToEventDetail: (String) -> Unit,
    onNavigateToFighterSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is HomeNavigationEvent.ToEventDetail -> onNavigateToEventDetail(event.eventId)
                is HomeNavigationEvent.ToFighterSearch -> onNavigateToFighterSearch()
            }
        }
    }

    HomeScreen(
        state = uiState,
        onAction = viewModel::onAction,
        onNavigateToSettings = onNavigateToSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val strings = LocalAppStrings.current
    val tabs = listOf(strings.tabUpcoming, strings.tabCompleted)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val upcomingListState = rememberLazyListState()
    val completedListState = rememberLazyListState()

    val onSearchClicked = remember(onAction) { { onAction(HomeUiAction.OnSearchClicked) } }
    val onRefreshUpcomingTab = remember(onAction) { { onAction(HomeUiAction.OnRefreshUpcomingTab) } }
    val onRefreshCompletedTab = remember(onAction) { { onAction(HomeUiAction.OnRefreshCompletedTab) } }
    val onEventClicked = remember(onAction) { { eventId: String -> onAction(HomeUiAction.OnEventClicked(eventId)) } }
    val onYearSelected = remember(onAction) { { year: Int -> onAction(HomeUiAction.OnYearSelected(year)) } }

    val errorMessage = when (state.error) {
        HomeError.NETWORK_ERROR -> strings.errorNetwork2
        HomeError.UNKNOWN_ERROR -> strings.errorUnknown
        null -> ""
    }
    ErrorSnackbarEffect(
        error = state.error,
        message = errorMessage,
        snackbarHostState = snackbarHostState,
        onRetry = onRefreshCompletedTab,
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    AppErrorSnackbar(
                        snackbarData = snackbarData,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            )
        },
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.eventsTopBarGradient)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = strings.eventsTitle,
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = AppFonts.RobotoCondensed,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    actions = {
                        IconButton(onClick = onSearchClicked) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = AppColors.textPrimary,
                            )
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = AppColors.textPrimary,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        titleContentColor = AppColors.textPrimary,
                    ),
                    scrollBehavior = scrollBehavior
                )

                AppTabRow(
                    tabs = tabs,
                    pagerState = pagerState,
                    coroutineScope = coroutineScope,
                    containerColor = Color.Transparent
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
