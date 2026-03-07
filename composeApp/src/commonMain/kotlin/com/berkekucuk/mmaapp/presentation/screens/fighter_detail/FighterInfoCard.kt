package com.berkekucuk.mmaapp.presentation.screens.fighter_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.model.Fighter
import mmaapp.composeapp.generated.resources.Res
import mmaapp.composeapp.generated.resources.fighter_detail_height_cm
import mmaapp.composeapp.generated.resources.fighter_detail_label_age
import mmaapp.composeapp.generated.resources.fighter_detail_label_born
import mmaapp.composeapp.generated.resources.fighter_detail_label_date_of_birth
import mmaapp.composeapp.generated.resources.fighter_detail_label_fighting_out_of
import mmaapp.composeapp.generated.resources.fighter_detail_label_height
import mmaapp.composeapp.generated.resources.fighter_detail_label_reach
import mmaapp.composeapp.generated.resources.fighter_detail_label_weight_class
import mmaapp.composeapp.generated.resources.fighter_detail_value_unavailable
import org.jetbrains.compose.resources.stringResource

@Composable
fun FighterInfoCard(
    fighter: Fighter,
    age: String?,
    formattedDob: String?,
    weightClassDisplay: String,
) {
    val unavailable = stringResource(Res.string.fighter_detail_value_unavailable)
    val rows = buildList {
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_weight_class),
                weightClassDisplay.ifEmpty { unavailable },
            )
        )
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_date_of_birth),
                formattedDob ?: unavailable,
            )
        )
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_age),
                age ?: unavailable,
            )
        )
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_height),
                fighter.height?.metric?.let {
                    stringResource(Res.string.fighter_detail_height_cm, it)
                } ?: unavailable,
            )
        )
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_reach),
                fighter.reach?.metric?.let {
                    stringResource(Res.string.fighter_detail_height_cm, it)
                } ?: unavailable,
            )
        )
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_born),
                fighter.born?.ifBlank { null } ?: unavailable,
            )
        )
        add(
            Pair(
                stringResource(Res.string.fighter_detail_label_fighting_out_of),
                fighter.fightingOutOf?.ifBlank { null } ?: unavailable,
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.fightItemBackground)
    ) {
        rows.forEachIndexed { index, (label, value) ->
            InfoRow(label = label, value = value)
            if (index < rows.lastIndex) {
                HorizontalDivider(
                    color = AppColors.dividerColor,
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.textSecondary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.textPrimary,
            textAlign = TextAlign.End,
        )
    }
}
