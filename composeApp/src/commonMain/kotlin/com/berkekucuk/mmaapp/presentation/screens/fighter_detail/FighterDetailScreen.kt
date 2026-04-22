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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.presentation.components.ErrorSnackbar
import com.berkekucuk.mmaapp.presentation.components.SnackbarEffect
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FighterDetailScreenRoot(
    viewModel: FighterDetailViewModel = koinViewModel(),
    onNavigateToFightDetail: (eventId: String, fightId: String, fighterId: String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FighterDetailNavigationEvent.ToFightDetail -> onNavigateToFightDetail(
                    event.eventId, event.fightId, event.fighterId
                )
                is FighterDetailNavigationEvent.Back -> onNavigateBack()
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
    val colors = LocalAppColors.current
    val tabs = listOf(strings.tabOverview, strings.tabFights)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val coroutineScope = rememberCoroutineScope()

    val onFightClicked = remember(onAction) { { eventId: String, fightId: String -> onAction(FighterDetailUiAction.OnFightClicked(eventId, fightId)) } }
    val onRefresh = remember(onAction) { { onAction(FighterDetailUiAction.OnRefresh) } }
    val onBackClick = remember(onAction) { { onAction(FighterDetailUiAction.OnBackClicked) } }

    val errorMessage = when (state.error) {
        FighterDetailError.NETWORK_ERROR -> strings.errorNetwork2
        FighterDetailError.UNKNOWN_ERROR -> strings.errorUnknown
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
                modifier = Modifier.background(colors.fighterBarBackground)
            ){
                MediumTopAppBar(
                    title = {
                        FighterTopBarTitle(
                            imageUrl = state.fighter?.imageUrl ?: "",
                            name = state.fighter?.name ?: "",
                            countryCode = state.fighter?.countryCode ?: "",
                            nickname = state.fighter?.nickname,
                        )
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
                        navigationIconContentColor = colors.textPrimary,
                        titleContentColor = colors.textPrimary,
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
                val fighter = state.fighter
                when (page) {
                    0 -> if (fighter != null) {
                        FighterOverviewContainer(
                            fighter = fighter,
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            extraBottomPadding = navBarBottomPadding,
                        )
                    } else {
                        ListContainer(
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            contentPadding = PaddingValues(),
                            extraBottomPadding = navBarBottomPadding,
                        ) {}
                    }
                    1 -> if (fighter != null) {
                        FighterHistoryContainer(
                            fighter = fighter,
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            onFightClicked = onFightClicked,
                            extraBottomPadding = navBarBottomPadding,
                        )
                    } else {
                        ListContainer(
                            isRefreshing = state.isRefreshing,
                            onRefresh = onRefresh,
                            contentPadding = PaddingValues(),
                            extraBottomPadding = navBarBottomPadding,
                        ) {}
                    }
                }
            }
        }
    }
}
