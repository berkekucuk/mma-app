package com.berkekucuk.mmaapp.presentation.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import com.berkekucuk.mmaapp.presentation.components.SnackbarEffect
import com.berkekucuk.mmaapp.presentation.screens.ranking_detail.RankedFighterRow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LeaderboardScreenRoot(
    viewModel: LeaderboardViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is LeaderboardNavigationEvent.Back -> onNavigateBack()
                is LeaderboardNavigationEvent.ToUserProfile -> onNavigateToProfile(event.userId)
            }
        }
    }

    LeaderboardScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardUiState,
    onAction: (LeaderboardUiAction) -> Unit
) {
    val onBackClicked = remember(onAction) { { onAction(LeaderboardUiAction.OnBackClicked) } }
    val onUserClicked = remember(onAction) { { userId: String -> onAction(LeaderboardUiAction.OnUserClicked(userId)) } }
    val onRefresh = remember(onAction) { { onAction(LeaderboardUiAction.OnRefresh) } }

    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    val snackbarHostState = remember { SnackbarHostState() }
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val errorMessage = when (state.error) {
        LeaderboardError.NETWORK_ERROR -> strings.errorNetwork2
        LeaderboardError.UNKNOWN_ERROR -> strings.errorUnknown
        null -> null
    }

    SnackbarEffect(
        message = errorMessage,
        snackbarHostState = snackbarHostState,
        actionLabel = strings.retry,
        onAction = onRefresh,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.pagerBackground,
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
                modifier = Modifier.background(colors.rankingTopBarGradient)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = strings.menuItemLeaderboard,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = colors.textPrimary,
                        titleContentColor = colors.textPrimary
                    )
                )
            }
        }
    ) { innerPadding ->
        LoadingContent(
            isLoading = state.isLoading && state.leaderboard.isEmpty(),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            ListContainer(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp),
                verticalSpacing = 0.dp,
                extraBottomPadding = navBarBottomPadding,
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(colors.fightItemBackground)
                    ) {
                        state.leaderboard.forEachIndexed { index, user ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                RankedFighterRow(
                                    rankNumber = index + 1,
                                    isChampion = false,
                                    name = user.fullName ?: user.username ?: "Unknown",
                                    record = user.username?.let { "@$it" } ?: "",
                                    imageUrl = user.avatarUrl ?: "",
                                    countryCode = null,
                                    trailingContent = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(end = 4.dp)
                                        ) {
                                            Text(
                                                text = user.totalPoints.toString(),
                                                color = colors.winColor,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.EmojiEvents,
                                                contentDescription = null,
                                                tint = colors.winColor,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    },
                                    onFighterClicked = { onUserClicked(user.id) }
                                )

                                if (index < state.leaderboard.lastIndex) {
                                    HorizontalDivider(
                                        color = colors.dividerColor,
                                        thickness = 0.8.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
