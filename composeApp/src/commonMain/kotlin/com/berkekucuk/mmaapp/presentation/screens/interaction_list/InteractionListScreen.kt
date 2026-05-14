package com.berkekucuk.mmaapp.presentation.screens.interaction_list

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import com.berkekucuk.mmaapp.presentation.components.AppAlertDialog
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import com.berkekucuk.mmaapp.presentation.components.SnackbarEffect
import com.berkekucuk.mmaapp.presentation.screens.ranking_detail.RankedFighterRow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InteractionListScreenRoot(
    viewModel: InteractionListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToFighterDetail: (String) -> Unit,
    onNavigateToFighterSearch: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is InteractionListNavigationEvent.Back -> onNavigateBack()
                is InteractionListNavigationEvent.ToAddFighter -> onNavigateToFighterSearch(event.interactionType)
                is InteractionListNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
            }
        }
    }

    InteractionListScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractionListScreen(
    state: InteractionListUiState,
    onAction: (InteractionListUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    val onBackClicked = remember(onAction) { { onAction(InteractionListUiAction.OnBackClicked) } }
    val onFighterClicked = remember(onAction) { { fighterId: String -> onAction(InteractionListUiAction.OnFighterClicked(fighterId)) } }
    val onAddFighterClicked = remember(onAction) { { onAction(InteractionListUiAction.OnAddFighterClicked) } }
    val onRemoveFighterClicked = remember(onAction) { { fighterId: String -> onAction(InteractionListUiAction.OnRemoveFighterClicked(fighterId)) } }
    val onConfirmRemove = remember(onAction) { { onAction(InteractionListUiAction.OnConfirmRemove) } }
    val onDismissRemove = remember(onAction) { { onAction(InteractionListUiAction.OnDismissRemove) } }
    val onRefresh = remember(onAction) { { onAction(InteractionListUiAction.OnRefresh) } }
    val onErrorDismissed = remember(onAction) { { onAction(InteractionListUiAction.OnErrorDismissed) } }
    val onDismissLimitAlert = remember(onAction) { { onAction(InteractionListUiAction.OnDismissLimitAlert) } }
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val errorMessage = strings.mapError(state.error)
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarEffect(
        message = errorMessage,
        snackbarHostState = snackbarHostState,
        duration = SnackbarDuration.Short,
        onDismiss = onErrorDismissed
    )

    if (state.deletingFighterId != null) {
        val fighterName = state.interactions.find { it.fighterId == state.deletingFighterId }?.fighter?.name ?: ""
        AppAlertDialog(
            onDismissRequest = onDismissRemove,
            onConfirmClick = onConfirmRemove,
            onDismissClick = onDismissRemove,
            text = strings.profileRemoveFighterConfirm(fighterName),
            confirmText = strings.commonRemove,
            dismissText = strings.commonCancel,
        )
    }

    if (state.showLimitAlert) {
        AppAlertDialog(
            onDismissRequest = onDismissLimitAlert,
            onConfirmClick = onDismissLimitAlert,
            title = strings.interactionLimitReachedTitle,
            text = strings.interactionLimitReachedText,
            confirmText = strings.dialogOkay,
        )
    }

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
            Column(modifier = Modifier.background(colors.rankingTopBar)) {
                TopAppBar(
                    title = {
                        val title = when (state.type) {
                            "favorite" -> strings.profileFavoriteFighters
                            "goat" -> strings.profileGoatFighters
                            "hated" -> strings.profileHatedFighters
                            else -> ""
                        }
                        Text(
                            text = strings.toUpperCase(title),
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
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(top = 8.dp),
            verticalSpacing = 0.dp,
            extraBottomPadding = navBarBottomPadding,
        ) {
            if (state.interactions.isEmpty()) {
                item(contentType = "EmptyState") {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = strings.emptyInteractionList,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.textSecondary
                        )
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(colors.fightItemBackground)
                    ) {
                        state.interactions.forEachIndexed { index, interaction ->
                            interaction.fighter?.let { fighter ->
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
                                                rankNumber = interaction.rankNumber ?: 0,
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

                                    if (index < state.interactions.lastIndex) {
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
}
