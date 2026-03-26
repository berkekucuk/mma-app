package com.berkekucuk.mmaapp.presentation.screens.event_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.core.utils.toUserFriendlyDate
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.SnackbarEffect
import com.berkekucuk.mmaapp.presentation.components.rememberLocalizedDateStrings
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventDetailScreenRoot(
    viewModel: EventDetailViewModel = koinViewModel(),
    onNavigateToFightDetail: (eventId: String, fightId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is EventDetailNavigationEvent.ToFightDetail -> onNavigateToFightDetail(event.eventId, event.fightId)
                is EventDetailNavigationEvent.Back -> onBackClick()
            }
        }
    }

    EventDetailScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    state: EventDetailUiState,
    onAction: (EventDetailUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val dateStrings = rememberLocalizedDateStrings()
    val eventTitleLine = remember(state.event?.name) {
        (state.event?.name ?: "").split(":", limit = 2)[0].trim()
    }
    val eventSubtitleLine = remember(state.event?.name) {
        (state.event?.name ?: "").split(":", limit = 2).getOrNull(1)?.trim()
    }

    val tabs = listOf(strings.tabMainCard, strings.tabPrelims)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val coroutineScope = rememberCoroutineScope()
    val mainCardListState = rememberLazyListState()
    val prelimsListState = rememberLazyListState()

    val onFightClick = remember(onAction) { { fightId: String -> onAction(EventDetailUiAction.OnFightClicked(fightId)) } }
    val onRefresh = remember(onAction) { { onAction(EventDetailUiAction.OnRefresh) } }
    val onBackClick = remember(onAction) { { onAction(EventDetailUiAction.OnBackClicked) } }

    val errorMessage = when (state.error) {
        EventDetailError.NETWORK_ERROR -> strings.errorNetwork2
        EventDetailError.UNKNOWN_ERROR -> strings.errorUnknown
        null -> null
    }
    SnackbarEffect(
        message = errorMessage,
        snackbarHostState = snackbarHostState,
        actionLabel = strings.retry,
        onAction = onRefresh,
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.statusBars,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = navBarBottomPadding),
                snackbar = { snackbarData ->
                    ErrorSnackbar(
                        snackbarData = snackbarData,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            )
        },
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.eventDetailTopBarGradient)
            ) {
                TopAppBar(
                    title = {
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = eventTitleLine.ifEmpty { strings.eventDetailsFallback },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            if (eventSubtitleLine != null) {
                                Text(
                                    text = eventSubtitleLine,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    color = AppColors.textSecondary,
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = AppColors.textPrimary,
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
                    0 -> FightsContainer(
                        fights = state.event?.mainCardFights ?: emptyList(),
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                        onFightClick = onFightClick,
                        emptyMessage = strings.emptyMainCardFights,
                        listState = mainCardListState,
                        eventDate = state.event?.datetimeUtc?.toUserFriendlyDate(dateStrings.months, dateStrings.daysOfWeek),
                        eventVenueAndLocation = listOfNotNull(state.event?.venue, state.event?.location).joinToString(", ").ifEmpty { null },
                        extraBottomPadding = navBarBottomPadding,
                    )
                    1 -> FightsContainer(
                        fights = state.event?.prelimFights ?: emptyList(),
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                        onFightClick = onFightClick,
                        emptyMessage = strings.emptyPrelimFights,
                        listState = prelimsListState,
                        eventDate = state.event?.datetimeUtc?.toUserFriendlyDate(dateStrings.months, dateStrings.daysOfWeek),
                        eventVenueAndLocation = listOfNotNull(state.event?.venue, state.event?.location).joinToString(", ").ifEmpty { null },
                        extraBottomPadding = navBarBottomPadding,
                    )
                }
            }
        }
    }
}