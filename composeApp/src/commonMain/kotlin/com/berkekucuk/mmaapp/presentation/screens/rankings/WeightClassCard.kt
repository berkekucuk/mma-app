package com.berkekucuk.mmaapp.presentation.screens.rankings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.AppFonts
import com.berkekucuk.mmaapp.domain.model.Ranking
import com.berkekucuk.mmaapp.presentation.components.FighterImage
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.content_description_collapse
import mmaapp.composeapp.generated.resources.content_description_expand
import mmaapp.composeapp.generated.resources.rankings_champion
import mmaapp.composeapp.generated.resources.rankings_champion_rank_label
import mmaapp.composeapp.generated.resources.rankings_vacant
import org.jetbrains.compose.resources.stringResource

@Composable
fun WeightClassCard(
    weightClassName: String,
    weightClassId: String,
    champion: Ranking?,
    rankedFighters: List<Ranking>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onFighterClicked: (String) -> Unit
) {
    val vacantText = stringResource(Res.string.rankings_vacant)
    val championLabel = stringResource(Res.string.rankings_champion)
    val championRankLabel = stringResource(Res.string.rankings_champion_rank_label)
    val collapseDescription = stringResource(Res.string.content_description_collapse)
    val expandDescription = stringResource(Res.string.content_description_expand)

    val championFighter = champion?.fighter
    val championName = remember(championFighter, vacantText) {
        (championFighter?.name ?: vacantText).uppercase()
    }
    val isPoundForPound = remember(weightClassId) {
        weightClassId == "mens_p4p" || weightClassId == "womens_p4p"
    }
    val showChampionInList = !isPoundForPound && championFighter != null
    val chevronIcon = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val chevronDescription = if (isExpanded) collapseDescription else expandDescription

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onToggleExpand() },
        border = BorderStroke(1.dp, AppColors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.cardHeaderBackground)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (championFighter != null) {
                    FighterImage(
                        imageUrl = championFighter.imageUrl,
                        name = championFighter.name,
                        countryCode = championFighter.countryCode,
                        result = null,
                        alignment = Alignment.Start
                    )

                    Spacer(Modifier.width(12.dp))
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = weightClassName.uppercase(),
                        color = AppColors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = championName,
                        color = AppColors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = championLabel,
                        color = AppColors.textSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Icon(
                    imageVector = chevronIcon,
                    contentDescription = chevronDescription,
                    tint = AppColors.textSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.fightItemBackground)
                        .padding(horizontal = 8.dp)
                        .padding(top = 4.dp, bottom = 4.dp)
                ) {
                    if (showChampionInList) {
                        RankedFighterRow(
                            rankLabel = championRankLabel,
                            name = championFighter.name,
                            record = championFighter.record.toString(),
                            imageUrl = championFighter.imageUrl,
                            countryCode = championFighter.countryCode,
                            rankChange = champion?.rankChange,
                            onFighterClicked = { onFighterClicked(championFighter.fighterId) }
                        )

                        if (rankedFighters.isNotEmpty()) {
                            HorizontalDivider(
                                color = AppColors.dividerColor,
                                thickness = 0.8.dp,
                            )
                        }
                    }

                    rankedFighters.forEachIndexed { index, ranking ->
                        ranking.fighter?.let { fighter ->
                            RankedFighterRow(
                                rankLabel = ranking.rankNumber.toString(),
                                name = fighter.name,
                                record = fighter.record.toString(),
                                imageUrl = fighter.imageUrl,
                                countryCode = fighter.countryCode,
                                rankChange = ranking.rankChange,
                                onFighterClicked = { onFighterClicked(fighter.fighterId) }
                            )

                            if (index < rankedFighters.lastIndex) {
                                HorizontalDivider(
                                    color = AppColors.dividerColor,
                                    thickness = 0.8.dp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
