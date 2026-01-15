package com.berkekucuk.mmaapp.presentation.screens.event_detail

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
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun EventDetailScreenRoot(
    viewModel: EventDetailViewModel = koinViewModel(),
    onNavigateToFightDetail: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is EventDetailNavigationEvent.ToFightDetail -> onNavigateToFightDetail(event.fightId)
                is EventDetailNavigationEvent.Back -> onBackClick()
            }
        }
    }

    EventDetailScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
fun EventDetailScreen(
    state: EventDetailUiState,
    onAction: (EventDetailUiAction) -> Unit,
) {
    val tabs = listOf("Main Card", "Prelims")
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val mainCardListState = rememberLazyListState()
    val prelimsListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.pagerBackground)
    ) {
        EventDetailTopBar(
            pagerState = pagerState,
            tabs = tabs,
            eventName = state.event?.name ?: "Event Details",
            onBackClick = { onAction(EventDetailUiAction.OnBackClicked) }
        )

        AnimatedContent(
            targetState = state.isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "EventDetailContentTransition",
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
                        0 -> FightsTab(
                            fights = state.mainCardFights,
                            emptyMessage = "No main card fights available",
                            listState = mainCardListState
                        )
                        1 -> FightsTab(
                            fights = state.prelimFights,
                            emptyMessage = "No prelim fights available",
                            listState = prelimsListState
                        )
                    }
                }
            }
        }
    }
}
