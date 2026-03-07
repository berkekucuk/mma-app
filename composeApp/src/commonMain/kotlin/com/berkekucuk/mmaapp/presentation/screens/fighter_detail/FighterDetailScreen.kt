package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.presentation.components.AppTabRow
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_back
import mmaapp.composeapp.generated.resources.tab_details
import mmaapp.composeapp.generated.resources.tab_fights
import org.jetbrains.compose.resources.stringResource
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

    val tabs = listOf(stringResource(Res.string.tab_details), stringResource(Res.string.tab_fights))
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val onBackClick = remember(onAction) { { onAction(FighterDetailUiAction.OnBackClicked) } }
    val onRefresh = remember(onAction) { { onAction(FighterDetailUiAction.OnRefresh) } }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.navigationBars,
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
                                nickname = fighter.nickname ?: " ",
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.content_description_back),
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
                    0 -> DetailsContainer(
                        fighter = state.fighter,
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                    )
                    1 -> FighterHistoryContainer(
                        fighter = state.fighter,
                        isRefreshing = state.isRefreshing,
                        onRefresh = onRefresh,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}
