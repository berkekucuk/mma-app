package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkekucuk.mmaapp.core.presentation.AppColors
import com.berkekucuk.mmaapp.domain.enums.Result
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.core.presentation.LocalAppStrings

@Composable
fun FightResultCard(
    fight: Fight,
    modifier: Modifier = Modifier,
) {
    val redCorner = fight.redCorner
    val blueCorner = fight.blueCorner

    val strings = LocalAppStrings.current
    val drawLabel = strings.fightResultDraw
    val noContestLabel = strings.fightResultNoContest

    val winner = remember(fight) {
        when {
            redCorner?.result == Result.WIN -> redCorner
            blueCorner?.result == Result.WIN -> blueCorner
            else -> null
        }
    }

    val loser = remember(winner) {
        if (winner == redCorner) blueCorner else redCorner
    }

    val resultLine = when {
        winner != null -> strings.fightResultDefeats(winner.fighter.name, loser?.fighter?.name ?: "")
        redCorner?.result == Result.DRAW -> drawLabel
        redCorner?.result == Result.NO_CONTEST -> noContestLabel
        else -> null
    }

    val methodRaw = remember(fight.methodType, fight.methodDetail, fight.roundSummary) {
        buildString {
            if (fight.methodType.isNotBlank()) {
                append(fight.methodType)
                if (fight.methodDetail.isNotBlank()) {
                    append(" (${fight.methodDetail})")
                }
            }
            if (fight.roundSummary.isNotBlank()) {
                if (isNotEmpty()) append(" • ")
                append(fight.roundSummary.replace("\n", " "))
            }
        }.ifBlank { null }
    }

    val methodLine = methodRaw?.let { strings.fightResultVia(it) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.fightItemBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        resultLine?.let {
            Text(
                text = it,
                color = AppColors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        methodLine?.let {
            Text(
                text = it,
                color = AppColors.dateColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

fun Fight.hasDisplayableResult(): Boolean {
    val hasWinner = redCorner?.result == Result.WIN || blueCorner?.result == Result.WIN
    val hasDraw = redCorner?.result == Result.DRAW
    val hasNoContest = redCorner?.result == Result.NO_CONTEST
    val hasMethod = methodType.isNotBlank() || roundSummary.isNotBlank()
    return hasWinner || hasDraw || hasNoContest || hasMethod
}