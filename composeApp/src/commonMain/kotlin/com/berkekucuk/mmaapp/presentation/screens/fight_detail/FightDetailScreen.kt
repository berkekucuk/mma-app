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
import androidx.compose.material3.LargeTopAppBar
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.text.style.TextOverflow
import com.berkekucuk.mmaapp.domain.enums.Result
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.derivedStateOf
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.clickable

@Composable
fun FightDetailScreenRoot(
    viewModel: FightDetailViewModel = koinViewModel(),
    onNavigateToFighterDetail: (fighterId: String) -> Unit,
    onNavigateToEvent: (eventId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FightDetailNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
                is FightDetailNavigationEvent.ToEvent -> onNavigateToEvent(event.eventId)
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
            LargeTopAppBar(
    title = {
        state.fight?.let { fight ->
            val fraction = scrollBehavior.state.collapsedFraction
            val redCorner = fight.redCorner
            val blueCorner = fight.blueCorner

            // W/L harfleri
            val redResultLetter = when (redCorner?.result) {
                Result.WIN -> "W"
                Result.LOSS -> "L"
                Result.DRAW -> "D"
                Result.NO_CONTEST -> "NC"
                else -> null
            }
            val blueResultLetter = when (blueCorner?.result) {
                Result.WIN -> "W"
                Result.LOSS -> "L"
                Result.DRAW -> "D"
                Result.NO_CONTEST -> "NC"
                else -> null
            }

            // W/L renkleri
            val redResultColor = when (redCorner?.result) {
                Result.WIN -> AppColors.winColor
                Result.LOSS -> AppColors.loseColor
                Result.DRAW -> AppColors.drawColor
                Result.NO_CONTEST -> AppColors.noContestColor
                else -> AppColors.textSecondary
            }
            val blueResultColor = when (blueCorner?.result) {
                Result.WIN -> AppColors.winColor
                Result.LOSS -> AppColors.loseColor
                Result.DRAW -> AppColors.drawColor
                Result.NO_CONTEST -> AppColors.noContestColor
                else -> AppColors.textSecondary
            }

            val hasResult = redResultLetter != null

            // Method bilgisi (ör: "UD | Raund 5, 3:06")
            val methodInfo = remember(fight.methodType, fight.roundSummary) {
                buildString {
                    if (fight.methodType.isNotBlank()) append(fight.methodType)
                    if (fight.roundSummary.isNotBlank()) {
                        if (isNotEmpty()) append(" | ")
                        append(fight.roundSummary.replace("\n", " "))
                    }
                }.ifBlank { null }
            }

            // Animasyonlar (fraction: 0=expanded, 1=collapsed)
            val imageSize = lerp(55.dp, 45.dp, fraction)
            val centerPadding = lerp(12.dp, 0.dp, fraction)
            val nameAlpha = (1f - fraction * 2.5f)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Red corner
                    FighterImage(
                        imageUrl = redCorner?.fighter?.imageUrl ?: "",
                        name = redCorner?.fighter?.name ?: "",
                        countryCode = redCorner?.fighter?.countryCode ?: "",
                        result = redCorner?.result?.name,
                        alignment = Alignment.Start,
                        imageSize = imageSize,
                        onClick = redCorner?.fighter?.fighterId?.let { fighterId ->
                            { onAction(FightDetailUiAction.OnFighterClicked(fighterId)) }
                        }
                    )

                    // Ortadaki alan: W/L veya VS
                    if (hasResult) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = centerPadding),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = redResultLetter!!,
                                    color = redResultColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                )
                                Text(
                                    text = " - ",
                                    color = AppColors.textSecondary,
                                    fontSize = 16.sp,
                                )
                                Text(
                                    text = blueResultLetter!!,
                                    color = blueResultColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                )
                            }
                            methodInfo?.let {
                                Text(
                                    text = it,
                                    color = AppColors.textSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "VS",
                            color = AppColors.textSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = centerPadding),
                        )
                    }

                    // Blue corner
                    FighterImage(
                        imageUrl = blueCorner?.fighter?.imageUrl ?: "",
                        name = blueCorner?.fighter?.name ?: "",
                        countryCode = blueCorner?.fighter?.countryCode ?: "",
                        result = blueCorner?.result?.name,
                        alignment = Alignment.End,
                        imageSize = imageSize,
                        onClick = blueCorner?.fighter?.fighterId?.let { fighterId ->
                            { onAction(FightDetailUiAction.OnFighterClicked(fighterId)) }
                        }
                    )
                }

                // Dövüşçü isimleri (sadece expanded'da)
                val nameAlpha = (1f - fraction * 2.5f).coerceIn(0f, 1f)
                if (nameAlpha > 0f) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .alpha(nameAlpha),
                    ) {
                        Text(
                            text = redCorner?.fighter?.name ?: "",
                            color = AppColors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(modifier = Modifier.weight(0.001f))
                        Text(
                            text = blueCorner?.fighter?.name ?: "",
                            color = AppColors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
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
                state.eventName?.let { eventName ->
                item(contentType = "EventName") {
                Text(
                    text = eventName,
                    color = AppColors.textSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAction(FightDetailUiAction.OnEventClicked) }
                        .padding(vertical = 8.dp)
        )
    }
}
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