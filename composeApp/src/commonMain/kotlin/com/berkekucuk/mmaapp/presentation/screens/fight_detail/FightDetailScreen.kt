package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
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
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FightDetailScreenRoot(
    viewModel: FightDetailViewModel = koinViewModel(),
    onNavigateToFighterDetail: (fighterId: String) -> Unit,
    onNavigateToEventDetail: (eventId: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { event ->
            when (event) {
                is FightDetailNavigationEvent.ToFighterDetail -> onNavigateToFighterDetail(event.fighterId)
                is FightDetailNavigationEvent.Back -> onBackClick()
                is FightDetailNavigationEvent.ToEventDetail -> onNavigateToEventDetail(event.eventId)
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
    val strings = LocalAppStrings.current
    val onBackClick = remember(onAction) { { onAction(FightDetailUiAction.OnBackClicked) } }
    val onRefresh = remember(onAction) { { onAction(FightDetailUiAction.OnRefresh) } }

    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            MediumTopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        state.fight?.let { fight ->
                            val redCorner = fight.redCorner?.fighter
                            val blueCorner = fight.blueCorner?.fighter
                            val isExpanded = scrollBehavior.state.collapsedFraction < 0.5f
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        redCorner?.fighterId?.let { id -> 
                                            onAction(FightDetailUiAction.OnFighterClicked(id))
                                        }
                                    }
                                ) {
                                    FighterImage(
                                        imageUrl = redCorner?.imageUrl ?: "",
                                        name = redCorner?.name ?: "",
                                        countryCode = redCorner?.countryCode ?: "",
                                        result = fight.redCorner?.result?.name,
                                        alignment = Alignment.Start,
                                        imageSize = if(isExpanded) 55.dp else 40.dp
                                    )
                                    if (isExpanded) {
                                        Text(
                                            text = redCorner?.name ?: "",
                                            color = AppColors.textPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                            .padding(top = 8.dp)
                                            .offset(x = (-12).dp)
                                        )
                                    }
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .offset(y = if (isExpanded) 0.dp else 10.dp)
                                ) {
                                    val redResult = fight.redCorner?.result?.name
                                    val blueResult = fight.blueCorner?.result?.name
                                    
                                    val hasWinnerOrLoser = redResult == "WIN" || blueResult == "WIN" || redResult == "LOSS" || blueResult == "LOSS"
                                    
                                    if (hasWinnerOrLoser && redResult != null && blueResult != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            val textSize = if (isExpanded) 20.sp else 18.sp
                                            Text(
                                                text = if (redResult == "WIN") "W" else "L",
                                                color = if (redResult == "WIN") AppColors.winColor else AppColors.loseColor,
                                                fontSize = textSize,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = " - ",
                                                color = AppColors.textSecondary,
                                                fontSize = textSize,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = if (blueResult == "WIN") "W" else "L",
                                                color = if (blueResult == "WIN") AppColors.winColor else AppColors.loseColor,
                                                fontSize = textSize,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        val methodText = buildString {
                                            if (fight.methodType.isNotBlank()) append(fight.methodType)
                                            if (fight.roundSummary.isNotBlank()) {
                                                if (isNotEmpty()) append(" | ")
                                                append(fight.roundSummary.replace("\n", " "))
                                            }
                                        }
                                        if (methodText.isNotBlank()) {
                                            Text(
                                                text = methodText,
                                                color = AppColors.textSecondary,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    } else if (redResult == "DRAW") {
                                         Text(
                                            text = "D - D",
                                            color = AppColors.drawColor,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    } else if (redResult == "NO_CONTEST") {
                                         Text(
                                            text = "NC",
                                            color = AppColors.noContestColor,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    } else {
                                        Text(
                                            text = "VS",
                                            color = AppColors.textSecondary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        blueCorner?.fighterId?.let { id -> 
                                            onAction(FightDetailUiAction.OnFighterClicked(id))
                                        }
                                    }) {
                                    FighterImage(
                                        imageUrl = blueCorner?.imageUrl ?: "",
                                        name = blueCorner?.name ?: "",
                                        countryCode = blueCorner?.countryCode ?: "",
                                        result = fight.blueCorner?.result?.name,
                                        alignment = Alignment.End,
                                        imageSize = if (isExpanded) 55.dp else 40.dp
                                    )
                                    if (isExpanded) {
                                        Text(
                                            text = blueCorner?.name ?: "",
                                            color = AppColors.textPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                            .padding(top = 8.dp)
                                            .offset(x = 12.dp)
                                        )
                                    }
                                }
                            }

                        }
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
                extraBottomPadding = navBarBottomPadding,
            ) {
                val displayEventName = state.eventName?: state.fight?.eventName
                displayEventName?.let {
                    eventName ->
                    item(contentType = "EventName") {
                        Text(
                            text = eventName,
                            color = AppColors.textSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val eventId = state.fight?.eventId
                                if (!eventId.isNullOrBlank())
                                onAction(FightDetailUiAction.OnEventClicked(eventId))
                            }
                            .padding(horizontal = 16.dp,
                            vertical = 12.dp),
                            textAlign = TextAlign.Center
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