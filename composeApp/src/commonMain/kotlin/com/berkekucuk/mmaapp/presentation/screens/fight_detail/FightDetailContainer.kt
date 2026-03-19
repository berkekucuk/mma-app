package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.core.utils.calculateAgeAtDate
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings
import com.berkekucuk.mmaapp.domain.model.Participant
import kotlin.time.Instant

@Composable
fun FightDetailContainer(
    redCorner: Participant?,
    blueCorner: Participant?,
    eventDate: Instant?,
    modifier: Modifier = Modifier,
) {
    val redFighter = redCorner?.fighter
    val blueFighter = blueCorner?.fighter

    val redFighterAge = remember(redFighter?.dateOfBirth, eventDate) {
        calculateAgeAtDate(redFighter?.dateOfBirth, eventDate)
    }
    val blueFighterAge = remember(blueFighter?.dateOfBirth, eventDate) {
        calculateAgeAtDate(blueFighter?.dateOfBirth, eventDate)
    }

    val showRecord = redCorner?.recordAfterFight != null || blueCorner?.recordAfterFight != null

    val strings = LocalAppStrings.current
    val rows = buildList {
        add(Triple(redFighter?.name, strings.fightDetailLabelName, blueFighter?.name))
        add(Triple(redFighterAge, strings.fightDetailLabelAge, blueFighterAge))
        add(Triple(redFighter?.fightingOutOf?.ifBlank { null }, strings.fightDetailLabelHometown, blueFighter?.fightingOutOf?.ifBlank { null }))
        add(Triple(redFighter?.height?.metric?.let { strings.heightCm(it.toString()) }, strings.fightDetailLabelHeight, blueFighter?.height?.metric?.let { strings.heightCm(it.toString()) }))
        add(Triple(redFighter?.reach?.metric?.let { strings.heightCm(it.toString()) }, strings.fightDetailLabelReach, blueFighter?.reach?.metric?.let { strings.heightCm(it.toString()) }))
        add(Triple(
            redCorner?.oddsValue?.let { val sign = if (it >= 0) "+" else ""; "$sign$it\n(${redCorner.oddsLabel})" },
            strings.fightDetailLabelOdds,
            blueCorner?.oddsValue?.let { val sign = if (it >= 0) "+" else ""; "$sign$it\n(${blueCorner.oddsLabel})" },
        ))
        add(Triple(redCorner?.result?.name?.let { strings.resultDisplayName(it) }, strings.fightDetailLabelResult, blueCorner?.result?.name?.let { strings.resultDisplayName(it) }))
        if (showRecord) {
            add(Triple(redCorner?.recordAfterFight?.toString(), strings.fightDetailLabelRecord, blueCorner?.recordAfterFight?.toString()))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.fightItemBackground)
    ) {
        Column {
            rows.forEachIndexed { index, (leftValue, label, rightValue) ->
                StatRow(
                    leftValue = leftValue ?: "—",
                    label = label,
                    rightValue = rightValue ?: "—",
                )
                if (index < rows.lastIndex) {
                    HorizontalDivider(
                        color = AppColors.dividerColor,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    leftValue: String,
    label: String,
    rightValue: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = leftValue,
            color = AppColors.textPrimary,
            fontSize = 12.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = label,
            color = AppColors.textSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1.5f),
        )

        Text(
            text = rightValue,
            color = AppColors.textPrimary,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}