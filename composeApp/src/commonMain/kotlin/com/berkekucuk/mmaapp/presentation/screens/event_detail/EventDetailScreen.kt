package com.berkekucuk.mmaapp.presentation.screens.event_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.utils.toUserFriendlyDate
import kotlinx.coroutines.launch
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_back
import mmaapp.composeapp.generated.resources.empty_main_card_fights
import mmaapp.composeapp.generated.resources.empty_prelim_fights
import mmaapp.composeapp.generated.resources.event_details_fallback
import mmaapp.composeapp.generated.resources.tab_main_card
import mmaapp.composeapp.generated.resources.tab_prelims
import org.jetbrains.compose.resources.stringResource
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
    val tabs = listOf(stringResource(Res.string.tab_main_card), stringResource(Res.string.tab_prelims))
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val mainCardListState = rememberLazyListState()
    val prelimsListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val onRefresh = remember(onAction) { { onAction(EventDetailUiAction.OnRefresh) } }
    val onFightClick = remember(onAction) { { fightId: String -> onAction(EventDetailUiAction.OnFightClicked(fightId)) } }
    val onBackClick = remember(onAction) { { onAction(EventDetailUiAction.OnBackClicked) } }

    val eventTitleLine = remember(state.event?.name) {
        (state.event?.name ?: "").split(":", limit = 2)[0].trim()
    }
    val eventSubtitleLine = remember(state.event?.name) {
        (state.event?.name ?: "").split(":", limit = 2).getOrNull(1)?.trim()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.topBarBackground)
            ) {
                TopAppBar(
                    title = {
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = eventTitleLine.ifEmpty { stringResource(Res.string.event_details_fallback) },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = AppColors.textPrimary
                            )
                            if (eventSubtitleLine != null) {
                                Text(
                                    text = eventSubtitleLine,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AppColors.textSecondary
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.content_description_back),
                                tint = AppColors.textPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColors.topBarBackground,
                        scrolledContainerColor = AppColors.topBarBackground,
                    ),
                    scrollBehavior = scrollBehavior
                )

                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = AppColors.topBarBackground,
                    contentColor = AppColors.textPrimary,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                            width = Dp.Unspecified,
                            height = 3.dp,
                            color = AppColors.ufcRed
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
                            },
                            selectedContentColor = AppColors.textPrimary,
                            unselectedContentColor = AppColors.textSecondary
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = state.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "EventDetailContentTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.ufcRed)
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> FightsContainer(
                            fights = state.mainCardFights,
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            onFightClick = onFightClick,
                            emptyMessage = stringResource(Res.string.empty_main_card_fights),
                            listState = mainCardListState,
                            eventDate = state.event?.datetimeUtc?.toUserFriendlyDate(),
                            eventVenueAndLocation = listOfNotNull(state.event?.venue, state.event?.location).joinToString(", ").ifEmpty { null }
                        )
                        1 -> FightsContainer(
                            fights = state.prelimFights,
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            onFightClick = onFightClick,
                            emptyMessage = stringResource(Res.string.empty_prelim_fights),
                            listState = prelimsListState,
                            eventDate = state.event?.datetimeUtc?.toUserFriendlyDate(),
                            eventVenueAndLocation = listOfNotNull(state.event?.venue, state.event?.location).joinToString(", ").ifEmpty { null }
                        )
                    }
                }
            }
        }
    }
}