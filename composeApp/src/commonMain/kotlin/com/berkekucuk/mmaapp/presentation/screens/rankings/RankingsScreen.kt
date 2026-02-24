package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.domain.model.RankedFighter
import com.berkekucuk.mmaapp.domain.model.WeightClassRanking
import com.berkekucuk.mmaapp.presentation.components.LoadingContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RankingsScreenRoot(
    viewModel: RankingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RankingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingsScreen(
    state: RankingsUiState,
    onAction: (RankingsUiAction) -> Unit
) {
    Scaffold(
        containerColor = AppColors.pagerBackground,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RANKINGS",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = AppFonts.RobotoCondensed,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.topBarBackground,
                    scrolledContainerColor = AppColors.topBarBackground,
                    titleContentColor = AppColors.textPrimary
                )
            )
        }
    ) { innerPadding ->
        LoadingContent(
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.rankings,
                    key = { it.weightClassId }
                ) { weightClass ->
                    WeightClassCard(
                        weightClassRanking = weightClass,
                        isExpanded = state.expandedWeightClasses.contains(weightClass.weightClassId),
                        onToggleExpand = {
                            onAction(RankingsUiAction.OnToggleExpand(weightClass.weightClassId))
                        }
                    )
                    HorizontalDivider(
                        color = AppColors.dividerColor,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun WeightClassCard(
    weightClassRanking: WeightClassRanking,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val context = LocalPlatformContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.pagerBackground)
    ) {
        // Champion header section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Left side: weight class name + champion name + "CHAMPION"
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 140.dp)
            ) {
                // Weight class name
                Text(
                    text = weightClassRanking.weightClassName.uppercase(),
                    color = AppColors.ufcRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = AppFonts.RobotoCondensed,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(4.dp))

                // Champion name
                Text(
                    text = weightClassRanking.champion.name.uppercase(),
                    color = AppColors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = AppFonts.RobotoCondensed,
                    lineHeight = 26.sp
                )

                Spacer(Modifier.height(4.dp))

                // "CHAMPION" label
                Text(
                    text = "CHAMPION",
                    color = AppColors.textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = AppFonts.RobotoCondensed,
                    letterSpacing = 1.5.sp
                )
            }

            // Right side: Champion image
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(weightClassRanking.champion.imageUrl)
                    .crossfade(true)
                    .size(200, 200)
                    .memoryCacheKey(weightClassRanking.champion.imageUrl)
                    .build(),
                contentDescription = weightClassRanking.champion.name,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 32.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(AppColors.topBarBackground),
                contentScale = ContentScale.Crop
            )

            // Chevron icon (bottom-right)
            Icon(
                imageVector = if (isExpanded)
                    Icons.Default.KeyboardArrowUp
                else
                    Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = AppColors.textPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
            )
        }

        // Expanded ranked fighters list
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            ) {
                HorizontalDivider(
                    color = AppColors.dividerColor,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                weightClassRanking.rankedFighters.forEach { fighter ->
                    RankedFighterRow(fighter = fighter)
                }
            }
        }
    }
}

@Composable
private fun RankedFighterRow(fighter: RankedFighter) {
    val context = LocalPlatformContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank number
        Text(
            text = "${fighter.rank}",
            color = AppColors.textSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = AppFonts.RobotoCondensed,
            modifier = Modifier.width(28.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.width(12.dp))

        // Fighter image (small circle)
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(fighter.imageUrl)
                .crossfade(true)
                .size(80, 80)
                .memoryCacheKey(fighter.imageUrl)
                .build(),
            contentDescription = fighter.name,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AppColors.topBarBackground),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        // Name and record
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fighter.name,
                color = AppColors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = AppFonts.RobotoCondensed
            )
            Text(
                text = fighter.record.toString(),
                color = AppColors.textSecondary,
                fontSize = 12.sp,
                fontFamily = AppFonts.RobotoCondensed
            )
        }
    }
}
