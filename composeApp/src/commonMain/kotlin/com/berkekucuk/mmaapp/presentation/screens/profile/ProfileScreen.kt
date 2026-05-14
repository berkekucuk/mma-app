package com.berkekucuk.mmaapp.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.toRankedFighter
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import com.berkekucuk.mmaapp.presentation.components.AppAlertDialog
import com.berkekucuk.mmaapp.domain.enums.ReportReason
import com.berkekucuk.mmaapp.core.utils.AppError
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import com.berkekucuk.mmaapp.presentation.screens.fighter_detail.FighterTopBarTitle
import com.berkekucuk.mmaapp.presentation.screens.rankings.WeightClassCard
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.SnackbarEffect
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToInteractionList: (String, String) -> Unit,
    onNavigateToFightDetail: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is ProfileNavigationEvent.Back -> onNavigateBack()
                is ProfileNavigationEvent.ToInteractionList -> onNavigateToInteractionList(event.userId, event.type)
                is ProfileNavigationEvent.ToFightDetail -> onNavigateToFightDetail(event.fightId)
            }
        }
    }

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    var showOverflowMenu by rememberSaveable { mutableStateOf(false) }
    val tabs = listOf(strings.profileTabOverview, strings.profileTabPredictions)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val onBackClicked = remember(onAction) { { onAction(ProfileUiAction.OnBackClicked) } }
    val onRefresh = remember(onAction) { { onAction(ProfileUiAction.OnRefresh) } }
    val onInteractionListClicked = remember(onAction) { { type: String -> onAction(ProfileUiAction.OnInteractionListClicked(type)) } }
    val onPredictionClicked = remember(onAction) { { fightId: String -> onAction(ProfileUiAction.OnPredictionClicked(fightId)) } }
    val onErrorDismissed = remember(onAction) { { onAction(ProfileUiAction.OnErrorDismissed) } }

    val isRetryableError = state.error == AppError.NETWORK
    val errorMessage = strings.mapError(state.error)
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarEffect(
        message = errorMessage,
        snackbarHostState = snackbarHostState,
        duration = if (isRetryableError) SnackbarDuration.Indefinite else SnackbarDuration.Short,
        actionLabel = if (isRetryableError) strings.retry else null,
        onAction = if (isRetryableError) onRefresh else null,
        onDismiss = if (!isRetryableError) onErrorDismissed else null
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = colors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
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
                modifier = Modifier.background(colors.fighterDetailTopBar)
            ) {
                MediumTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.contentDescriptionBack
                            )
                        }
                    },
                    title = {
                        state.profile?.user.let {
                            FighterTopBarTitle(
                                imageUrl = it?.avatarUrl ?: "",
                                name = it?.fullName ?: it?.username ?: "",
                                countryCode = "",
                                nickname = "@${it?.username ?: ""}",
                                showQuotes = false,
                            )
                        }
                    },
                    actions = {
                        if (!state.isCurrentUser) {
                            Box {
                                IconButton(onClick = { showOverflowMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options"
                                    )
                                }
                                DropdownMenu(
                                    expanded = showOverflowMenu,
                                    onDismissRequest = { showOverflowMenu = false },
                                    containerColor = colors.dropdownMenuBackground
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(strings.reportUserTitle, color = colors.textPrimary) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Report,
                                                contentDescription = strings.reportUserTitle,
                                                tint = colors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            showOverflowMenu = false
                                            onAction(ProfileUiAction.OnReportClicked)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(strings.blockUserTitle, color = colors.textPrimary) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Block,
                                                contentDescription = strings.blockUserTitle,
                                                tint = colors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            showOverflowMenu = false
                                            onAction(ProfileUiAction.OnBlockClicked)
                                        }
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = colors.textPrimary,
                        titleContentColor = colors.textPrimary,
                        actionIconContentColor = colors.textPrimary
                    ),
                    scrollBehavior = scrollBehavior,
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
                    .background(colors.pagerBackground),
                beyondViewportPageCount = 1
            ) { page ->
                when (page) {
                    0 -> {
                        ListContainer(
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            contentPadding = PaddingValues(top = 16.dp),
                            extraBottomPadding = navBarBottomPadding,
                        ) {
                            item {
                                WeightClassCard(
                                    weightClassName = strings.toUpperCase(strings.profileFavoriteFighters),
                                    champion = state.profile?.topFavorite?.toRankedFighter(),
                                    onWeightClassClicked = { onInteractionListClicked("favorite") },
                                )
                            }
                            item {
                                WeightClassCard(
                                    weightClassName = strings.toUpperCase(strings.profileGoatFighters),
                                    champion = state.profile?.topGoat?.toRankedFighter(),
                                    onWeightClassClicked = { onInteractionListClicked("goat") },
                                )
                            }
                            item {
                                WeightClassCard(
                                    weightClassName = strings.toUpperCase(strings.profileHatedFighters),
                                    champion = state.profile?.topHated?.toRankedFighter(),
                                    onWeightClassClicked = { onInteractionListClicked("hated") },
                                )
                            }
                        }
                    }
                    1 -> {
                        val predictions = state.profile?.predictions ?: emptyList()
                        ListContainer(
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            contentPadding = PaddingValues(top = 16.dp),
                            extraBottomPadding = navBarBottomPadding,
                        ) {
                            if (predictions.isEmpty()) {
                                item(contentType = "EmptyState") {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = strings.emptyPredictionList,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = colors.textSecondary
                                        )
                                    }
                                }
                            } else {
                                items(
                                    items = predictions,
                                    key = { it.predictionId }
                                ) { prediction ->
                                    PredictionCard(
                                        prediction = prediction,
                                        onClick = {
                                            prediction.fight?.let { fight ->
                                                onPredictionClicked(fight.fightId)
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showBlockDialog) {
        AppAlertDialog(
            onDismissRequest = { onAction(ProfileUiAction.OnDismissBlockDialog) },
            onConfirmClick = { onAction(ProfileUiAction.OnConfirmBlock) },
            title = strings.blockUserTitle,
            text = strings.blockUserConfirm,
            confirmText = strings.dialogAccept,
            dismissText = strings.commonCancel,
            isDestructive = true
        )
    }

    if (state.showReportDialog) {
        AppAlertDialog(
            onDismissRequest = { onAction(ProfileUiAction.OnDismissReportDialog) },
            onConfirmClick = { onAction(ProfileUiAction.OnSubmitReport) },
            title = strings.reportUserTitle,
            confirmText = strings.reportUserSubmit,
            dismissText = strings.commonCancel,
            confirmEnabled = state.reportReason != null,
            isConfirmLoading = state.isReporting,
            isDestructive = true,
            content = {
                Column(modifier = Modifier.selectableGroup()) {
                    ReportReason.entries.forEach { reason ->
                        val displayName = strings.reportReasonDisplayName(reason)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (reason == state.reportReason),
                                    onClick = { onAction(ProfileUiAction.OnReportReasonChanged(reason)) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (reason == state.reportReason),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colors.textPrimary,
                                    unselectedColor = colors.textSecondary
                                )
                            )
                            Text(
                                text = displayName,
                                color = colors.textPrimary,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}
