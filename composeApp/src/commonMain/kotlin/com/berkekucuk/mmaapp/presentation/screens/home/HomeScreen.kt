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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.core.presentation.AppLanguage
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToEventDetail: (String) -> Unit,
    onNavigateToFighterSearch: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
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
        onLanguageChange = onLanguageChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
) {
    val strings = LocalAppStrings.current
    val currentLanguage = strings.language
    val tabs = listOf(strings.tabUpcoming, strings.tabCompleted)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val upcomingListState = rememberLazyListState()
    val completedListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var menuExpanded by remember { mutableStateOf(false) }

    val onSearchClicked = remember(onAction) { { onAction(HomeUiAction.OnSearchClicked) } }
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
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = AppColors.textPrimary,
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            containerColor = AppColors.dropdownMenuBackground,
                            modifier = Modifier.width(IntrinsicSize.Min),
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "English",
                                        color = if (currentLanguage == AppLanguage.EN) AppColors.ufcRed else AppColors.textPrimary,
                                        fontWeight = if (currentLanguage == AppLanguage.EN) FontWeight.Bold else FontWeight.Normal,
                                    )
                                },
                                onClick = {
                                    onLanguageChange(AppLanguage.EN)
                                    menuExpanded = false
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Türkçe",
                                        color = if (currentLanguage == AppLanguage.TR) AppColors.ufcRed else AppColors.textPrimary,
                                        fontWeight = if (currentLanguage == AppLanguage.TR) FontWeight.Bold else FontWeight.Normal,
                                    )
                                },
                                onClick = {
                                    onLanguageChange(AppLanguage.TR)
                                    menuExpanded = false
                                },
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
