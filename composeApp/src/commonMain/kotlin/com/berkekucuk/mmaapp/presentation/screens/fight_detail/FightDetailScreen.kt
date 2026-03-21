package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.FightItem
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FightDetailScreenRoot(
    viewModel: FightDetailViewModel = koinViewModel(),
    onNavigateToFighterDetail: (fighterId: String) -> Unit,
    onNavigateToEventDetail: (eventId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FightDetailNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
                is FightDetailNavigationEvent.Back -> onBackClick()
                is FightDetailNavigationEvent.ToEventDetail -> onNavigateToEventDetail(event.eventId)
            }
        }
    }

    FightDetailScreen(
        state = uiState,
        cameFromEvent = viewModel.cameFromEvent,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FightDetailScreen(
    state: FightDetailUiState,
    cameFromEvent: Boolean,
    onAction: (FightDetailUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val onBackClick = remember(onAction) { { onAction(FightDetailUiAction.OnBackClicked) } }
    val onRefresh = remember(onAction) { { onAction(FightDetailUiAction.OnRefresh) } }
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val eventId = state.fight?.eventId
    val displayTitle = state.eventName ?: state.fight?.eventName
    val fight = state.fight
    val hasMetaInfo = fight != null && (
        fight.roundsFormat.isNotBlank() ||
        fight.roundSummary.isNotBlank() ||
        fight.weightClassLbs != null
    )
    val onRedCornerClick = remember(onAction, fight) {
        fight?.redCorner?.fighter?.fighterId?.let { id -> { onAction(FightDetailUiAction.OnFighterClicked(id)) } }
    }
    val onBlueCornerClick = remember(onAction, fight) {
        fight?.blueCorner?.fighter?.fighterId?.let { id -> { onAction(FightDetailUiAction.OnFighterClicked(id)) } }
    }
    val onEventLinkClick = remember(onAction, eventId, cameFromEvent) {
        {
            if (cameFromEvent) {
                onAction(FightDetailUiAction.OnBackClicked)
            } else if (!eventId.isNullOrBlank()) {
                onAction(FightDetailUiAction.OnEventClicked(eventId))
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            Column(modifier = Modifier.background(AppColors.eventDetailTopBarGradient)) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = AppColors.textPrimary,
                        actionIconContentColor = AppColors.textPrimary,
                        titleContentColor = AppColors.textPrimary,
                    ),
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { innerPadding ->
        LoadingContent(
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ListContainer(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                contentPadding = PaddingValues(top = 8.dp),
                verticalSpacing = 8.dp,
                extraBottomPadding = navBarBottomPadding,
            ) {
                item(contentType = "FightCard") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppColors.fightItemBackground),
                    ) {
                        if (fight != null) {
                            FightItem(
                                fight = fight,
                                modifier = Modifier.height(108.dp),
                                onRedCornerClick = onRedCornerClick,
                                onBlueCornerClick = onBlueCornerClick,
                            )
                        }
                        HorizontalDivider(
                            color = AppColors.dividerColor,
                            thickness = 0.8.dp,
                        )
                        FightDetailContainer(
                            redCorner = fight?.redCorner,
                            blueCorner = fight?.blueCorner,
                            eventDate = state.eventDate,
                        )
                    }
                }
                if (hasMetaInfo) {
                    item(contentType = "FightMetaCard") {
                        FightMetaCard(fight = fight)
                    }
                }
                if (!eventId.isNullOrBlank() && displayTitle != null) {
                    item(contentType = "EventLink") {
                        EventLinkRow(
                            eventName = displayTitle,
                            isBackNavigation = cameFromEvent,
                            onClick = onEventLinkClick,
                        )
                    }
                }
            }
        }
    }
}