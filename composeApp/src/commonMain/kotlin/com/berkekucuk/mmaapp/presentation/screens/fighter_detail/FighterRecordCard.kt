package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.model.Record
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.fighter_detail_record_draws
import mmaapp.composeapp.generated.resources.fighter_detail_record_losses
import mmaapp.composeapp.generated.resources.fighter_detail_record_wins
import org.jetbrains.compose.resources.stringResource

@Composable
fun FighterRecordCard(
    record: Record,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.fightItemBackground)
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatItem(
            value = record.wins.toString(),
            label = stringResource(Res.string.fighter_detail_record_wins),
            valueColor = AppColors.winColor,
        )
        StatItem(
            value = record.losses.toString(),
            label = stringResource(Res.string.fighter_detail_record_losses),
            valueColor = AppColors.loseColor,
        )
        StatItem(
            value = record.draws.toString(),
            label = stringResource(Res.string.fighter_detail_record_draws),
            valueColor = AppColors.textSecondary,
        )
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    valueColor: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.textSecondary,
        )
    }
}
