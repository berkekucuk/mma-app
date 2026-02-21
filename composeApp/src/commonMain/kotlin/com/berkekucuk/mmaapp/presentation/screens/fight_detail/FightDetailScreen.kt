package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.presentation.components.FighterImage
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_back
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FightDetailScreenRoot(
    viewModel: FightDetailViewModel = koinViewModel(),
    onNavigateToFighterDetail: (fighterId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FightDetailNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
                is FightDetailNavigationEvent.Back -> onBackClick()
            }
        }
    }

    FightDetailScreen(
        state = uiState,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FightDetailScreen(
    state: FightDetailUiState,
    onAction: (FightDetailUiAction) -> Unit,
) {
    val onBackClick = remember(onAction) { { onAction(FightDetailUiAction.OnBackClicked) } }
    val onRefresh = remember(onAction) { { onAction(FightDetailUiAction.OnRefresh) } }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            MediumTopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FighterImage(
                                imageUrl = state.fight?.redCorner?.fighter?.imageUrl,
                                name = state.fight?.redCorner?.fighter?.name,
                                countryCode = state.fight?.redCorner?.fighter?.countryCode,
                                result = state.fight?.redCorner?.result?.name,
                                alignment = Alignment.Start,
                            )

                            Text(
                                text = "VS",
                                color = AppColors.textSecondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 50.dp),
                            )

                            FighterImage(
                                imageUrl = state.fight?.blueCorner?.fighter?.imageUrl,
                                name = state.fight?.blueCorner?.fighter?.name,
                                countryCode = state.fight?.blueCorner?.fighter?.countryCode,
                                result = state.fight?.blueCorner?.result?.name,
                                alignment = Alignment.End,
                            )
                        }
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
                    containerColor = AppColors.topBarBackground,
                    scrolledContainerColor = AppColors.topBarBackground,
                    navigationIconContentColor = AppColors.textPrimary,
                    titleContentColor = AppColors.textPrimary,
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        LoadingContent(
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListContainer(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                contentPadding = PaddingValues(top = 10.dp),
            ) {
                state.fight?.takeIf { it.hasDisplayableResult() }?.let { fight ->
                    item(contentType = "FightResultCard") {
                        FightResultCard(fight = fight)
                    }
                }
                item(contentType = "FightDetailContainer") {
                    FightDetailContainer(
                        redCorner = state.fight?.redCorner,
                        blueCorner = state.fight?.blueCorner,
                        eventDate = state.eventDate,
                    )
                }
            }
        }
    }
}