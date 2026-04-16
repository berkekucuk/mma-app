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
import com.berkekucuk.mmaapp.core.presentation.LocalMeasurementUnit
import com.berkekucuk.mmaapp.core.presentation.MeasurementUnit
import com.berkekucuk.mmaapp.core.presentation.colors.LocalAppColors
import com.berkekucuk.mmaapp.core.presentation.strings.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.Fighter

@Composable
fun FighterInfoCard(
    fighter: Fighter,
    age: String?,
    formattedDob: String?,
    weightClassDisplay: String,
) {
    val strings = LocalAppStrings.current
    val colors = LocalAppColors.current
    val measurementUnit = LocalMeasurementUnit.current
    val unavailable = strings.fighterDetailValueUnavailable
    val rows = buildList {
        add(Pair(strings.fighterDetailLabelWeightClass, weightClassDisplay.ifEmpty { unavailable }))
        add(Pair(strings.fighterDetailLabelDateOfBirth, formattedDob ?: unavailable))
        add(Pair(strings.fighterDetailLabelAge, age ?: unavailable))
        add(Pair(strings.fighterDetailLabelHeight, when (measurementUnit) {
            MeasurementUnit.METRIC -> fighter.height.metric?.let { strings.heightCm(it.toString()) } ?: unavailable
            MeasurementUnit.IMPERIAL -> fighter.height.imperial?.ifBlank { null } ?: unavailable
        }))
        add(Pair(strings.fighterDetailLabelReach, when (measurementUnit) {
            MeasurementUnit.METRIC -> fighter.reach.metric?.let { strings.heightCm(it.toString()) } ?: unavailable
            MeasurementUnit.IMPERIAL -> fighter.reach.imperial?.ifBlank { null } ?: unavailable
        }))
        add(Pair(strings.fighterDetailLabelBorn, fighter.born?.ifBlank { null } ?: unavailable))
        add(Pair(strings.fighterDetailLabelFightingOutOf, fighter.fightingOutOf?.ifBlank { null } ?: unavailable))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.fightItemBackground)
    ) {
        rows.forEachIndexed { index, (label, value) ->
            InfoRow(label = label, value = value)
            if (index < rows.lastIndex) {
                HorizontalDivider(
                    color = colors.dividerColor,
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
    val colors = LocalAppColors.current

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
            color = colors.textSecondary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textPrimary,
            textAlign = TextAlign.End,
        )
    }
}
