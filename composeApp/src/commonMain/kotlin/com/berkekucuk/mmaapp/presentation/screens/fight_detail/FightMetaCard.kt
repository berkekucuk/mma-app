package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.Fight

@Composable
fun FightMetaCard(
    fight: Fight,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current

    data class MetaStat(val value: String, val label: String)

    val stats = buildList {
        if (fight.roundsFormat.isNotBlank()) add(MetaStat(fight.roundsFormat, strings.fightDetailLabelRoundsFormat))
        if (fight.roundSummary.isNotBlank()) add(MetaStat(fight.roundSummary.replace("\n", " "), strings.fightDetailLabelRoundSummary))
        fight.weightClassLbs?.let { add(MetaStat("$it lbs", strings.fighterDetailLabelWeightClass)) }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.fightItemBackground)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        stats.forEachIndexed { index, stat ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.textPrimary,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stat.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            if (index < stats.lastIndex) {
                VerticalDivider(
                    color = AppColors.dividerColor,
                    thickness = 1.dp,
                    modifier = Modifier.height(36.dp),
                )
            }
        }
    }
}