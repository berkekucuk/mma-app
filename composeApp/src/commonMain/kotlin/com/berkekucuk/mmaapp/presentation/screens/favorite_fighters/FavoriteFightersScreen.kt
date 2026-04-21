package com.berkekucuk.mmaapp.presentation.screens.favorite_fighters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.berkekucuk.mmaapp.presentation.components.SnackbarEffect
import com.berkekucuk.mmaapp.presentation.screens.ranking_detail.RankedFighterRow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteFightersScreenRoot(
    viewModel: FavoriteFightersViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToFighterDetail: (String) -> Unit,
    onNavigateToAddFighter: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FavoriteFightersNavigationEvent.Back -> onNavigateBack()
                is FavoriteFightersNavigationEvent.ToAddFighter -> onNavigateToAddFighter()
                is FavoriteFightersNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
            }
        }
    }

    FavoriteFightersScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteFightersScreen(
    state: FavoriteFightersUiState,
    onAction: (FavoriteFightersUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    val onBackClicked = remember(onAction) { { onAction(FavoriteFightersUiAction.OnBackClicked) } }
    val onFighterClicked = remember(onAction) { { fighterId: String -> onAction(FavoriteFightersUiAction.OnFighterClicked(fighterId)) } }
    val onAddFighterClicked = remember(onAction) { { onAction(FavoriteFightersUiAction.OnAddFighterClicked) } }
    val onRemoveFighterClicked = remember(onAction) { { fighterId: String -> onAction(FavoriteFightersUiAction.OnRemoveFighterClicked(fighterId)) } }
    val onRefresh = remember(onAction) { { onAction(FavoriteFightersUiAction.OnRefresh) } }
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage = when (state.error) {
        FavoriteFightersError.NETWORK_ERROR -> strings.errorNetwork
        FavoriteFightersError.UNKNOWN_ERROR -> strings.errorUnknown
        null -> null
    }

    SnackbarEffect(
        message = errorMessage,
        snackbarHostState = snackbarHostState,
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
            Column(modifier = Modifier.background(colors.rankingTopBarGradient)) {
                TopAppBar(
                    title = {
                        Text(
                            text = strings.toUpperCase(strings.profileFavoriteFighters),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack,
                            )
                        }
                    },
                    actions = {
                        if (state.isOwner) {
                            IconButton(onClick = onAddFighterClicked) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = colors.textPrimary,
                        titleContentColor = colors.textPrimary,
                        actionIconContentColor = colors.textPrimary,
                    ),
                )
            }
        }
    ) { innerPadding ->
        ListContainer(
            isRefreshing = false,
            onRefresh = onRefresh,
            modifier = Modifier.padding(innerPadding),
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
                    state.fighters.forEachIndexed { index, rankedFighter ->
                        rankedFighter.fighter?.let { fighter ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        RankedFighterRow(
                                            rankNumber = rankedFighter.rankNumber,
                                            isChampion = false,
                                            name = fighter.name,
                                            record = fighter.record.toString(),
                                            imageUrl = fighter.imageUrl,
                                            countryCode = fighter.countryCode,
                                            onFighterClicked = { onFighterClicked(fighter.fighterId) },
                                        )
                                    }
                                    if (state.isOwner) {
                                        IconButton(onClick = { onRemoveFighterClicked(fighter.fighterId) }) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = null,
                                                tint = colors.textSecondary,
                                            )
                                        }
                                    }
                                }

                                if (index < state.fighters.lastIndex) {
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
