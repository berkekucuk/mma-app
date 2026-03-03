package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.rankings_title
import mmaapp.composeapp.generated.resources.tab_mens
import mmaapp.composeapp.generated.resources.tab_womens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RankingsScreenRoot(
    viewModel: RankingViewModel = koinViewModel(),
    onNavigateToFighterDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is RankingNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
            }
        }
    }

    RankingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingsScreen(
    state: RankingUiState,
    onAction: (RankingUiAction) -> Unit
) {
    val tabs = listOf(stringResource(Res.string.tab_mens), stringResource(Res.string.tab_womens))
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val mensListState = rememberLazyListState()
    val womensListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val onRefresh = remember(onAction) { { onAction(RankingUiAction.OnRefresh) } }
    val onToggleExpand = remember(onAction) { { weightClassId: String -> onAction(RankingUiAction.OnToggleExpand(weightClassId)) } }
    val onFighterClicked = remember(onAction) { { fighterId: String -> onAction(RankingUiAction.OnFighterClicked(fighterId)) } }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.topBarBackground)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.rankings_title).uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = AppFonts.RobotoCondensed,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColors.topBarBackground,
                        scrolledContainerColor = AppColors.topBarBackground,
                        titleContentColor = AppColors.textPrimary
                    ),
                    scrollBehavior = scrollBehavior
                )

                AppTabRow(
                    tabs = tabs,
                    pagerState = pagerState,
                    coroutineScope = coroutineScope
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
                    0 -> RankingContainer(
                        rankings = state.mensRankings,
                        expandedWeightClasses = state.expandedWeightClasses,
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                        onToggleExpand = onToggleExpand,
                        onFighterClicked = onFighterClicked,
                        listState = mensListState
                    )
                    1 -> RankingContainer(
                        rankings = state.womensRankings,
                        expandedWeightClasses = state.expandedWeightClasses,
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                        onToggleExpand = onToggleExpand,
                        onFighterClicked = onFighterClicked,
                        listState = womensListState
                    )
                }
            }
        }
    }
}
