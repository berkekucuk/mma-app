package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.AppErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbarEffect
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FighterDetailScreenRoot(
    viewModel: FighterDetailViewModel = koinViewModel(),
    onNavigateToFightDetail: (eventId: String, fightId: String, fighterId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FighterDetailNavigationEvent.ToFightDetail -> onNavigateToFightDetail(
                    event.eventId, event.fightId, event.fighterId
                )
                is FighterDetailNavigationEvent.Back -> onBackClick()
            }
        }
    }

    FighterDetailScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FighterDetailScreen(
    state: FighterDetailUiState,
    onAction: (FighterDetailUiAction) -> Unit,
) {
    val strings = LocalAppStrings.current
    val tabs = listOf(strings.tabOverview, strings.tabFights)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    val onBackClick = remember(onAction) { { onAction(FighterDetailUiAction.OnBackClicked) } }
    val onRefresh = remember(onAction) { { onAction(FighterDetailUiAction.OnRefresh) } }
    val onFightClicked = remember(onAction) { { eventId: String, fightId: String -> onAction(FighterDetailUiAction.OnFightClicked(eventId, fightId)) } }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage = when (state.error) {
        FighterDetailError.NETWORK_ERROR -> strings.errorNetwork2
        FighterDetailError.UNKNOWN_ERROR -> strings.errorUnknown
        null -> ""
    }
    ErrorSnackbarEffect(
        error = state.error,
        message = errorMessage,
        snackbarHostState = snackbarHostState,
        onRetry = onRefresh,
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
                    AppErrorSnackbar(
                        snackbarData = snackbarData,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            )
        },
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.fighterBarBackground)
            ){
                MediumTopAppBar(
                    title = {
                        state.fighter?.let { fighter ->
                            FighterTopBarTitle(
                                imageUrl = fighter.imageUrl,
                                name = fighter.name,
                                countryCode = fighter.countryCode,
                                nickname = fighter.nickname,
                            )
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
                    .background(AppColors.pagerBackground),
                beyondViewportPageCount = 1
            ) { page ->
                when (page) {
                    0 -> state.fighter?.let { fighter ->
                        FighterOverviewContainer(
                            fighter = fighter,
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            extraBottomPadding = navBarBottomPadding,
                        )
                    }
                    1 -> state.fighter?.let { fighter ->
                        FighterHistoryContainer(
                            fighter = fighter,
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            onFightClicked = onFightClicked,
                            extraBottomPadding = navBarBottomPadding,
                        )
                    }
                }
            }
        }
    }
}
