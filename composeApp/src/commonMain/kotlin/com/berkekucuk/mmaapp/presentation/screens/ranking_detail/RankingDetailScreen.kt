package com.berkekucuk.mmaapp.presentation.screens.ranking_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import com.berkekucuk.mmaapp.presentation.components.ListContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.berkekucuk.mmaapp.core.presentation.AppColors
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_back
import mmaapp.composeapp.generated.resources.rankings_champion_rank_label
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RankingDetailScreenRoot(
    viewModel: RankingDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToFighterDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is RankingDetailNavigationEvent.Back -> onNavigateBack()
                is RankingDetailNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
            }
        }
    }

    RankingDetailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingDetailScreen(
    state: RankingDetailUiState,
    onAction: (RankingDetailUiAction) -> Unit
) {
    val onBackClicked = remember(onAction) { { onAction(RankingDetailUiAction.OnBackClicked) } }
    val onFighterClicked = remember(onAction) { { fighterId: String -> onAction(RankingDetailUiAction.OnFighterClicked(fighterId)) } }
    val onRefresh = remember(onAction) { { onAction(RankingDetailUiAction.OnRefresh) } }

    val championRankLabel = stringResource(Res.string.rankings_champion_rank_label)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.pagerBackground,
        topBar = {
            Column(
                modifier = Modifier.background(AppColors.rankingTopBarGradient)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = state.weightClassName.uppercase(),
                            fontSize = 20.sp,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.content_description_back)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = AppColors.textPrimary,
                        titleContentColor = AppColors.textPrimary
                    )
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
            ListContainer(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                contentPadding = PaddingValues(top = 8.dp),
                verticalSpacing = 0.dp,
            ) {
                item {
                    val displayedFighters = if (state.isPoundForPound) {
                        state.rankedFighters.filter { it.rankNumber > 0 }
                    } else {
                        state.rankedFighters
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AppColors.fightItemBackground)
                    ) {
                        displayedFighters.forEachIndexed { index, ranking ->
                            ranking.fighter?.let { fighter ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    val rankLabelStr = if (ranking.rankNumber == 0) championRankLabel else ranking.rankNumber.toString()

                                    RankedFighterRow(
                                        rankLabel = rankLabelStr,
                                        name = fighter.name,
                                        record = fighter.record.toString(),
                                        imageUrl = fighter.imageUrl,
                                        countryCode = fighter.countryCode,
                                        onFighterClicked = { onFighterClicked(fighter.fighterId) }
                                    )

                                    if (index < displayedFighters.lastIndex) {
                                        HorizontalDivider(
                                            color = AppColors.dividerColor,
                                            thickness = 0.5.dp,
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
