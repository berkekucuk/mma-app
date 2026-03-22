package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import com.berkekucuk.mmaapp.domain.enums.Result
import com.berkekucuk.mmaapp.domain.model.Fighter

data class FighterRates(
    val finishRate: Float,
    val koTkoRate: Float,
    val submissionRate: Float,
)

fun Fighter.calculateRates(): FighterRates {
    val wins = fights.filter { fight ->
        fight.participants.any { p ->
            p.fighter.fighterId == this.fighterId && p.result == Result.WIN
        }
    }

    val totalWins = wins.size
    if (totalWins == 0) return FighterRates(0f, 0f, 0f)

    val koWins = wins.count { it.methodType.contains("ko", ignoreCase = true) }
    val submissionWins = wins.count { it.methodType.contains("sub", ignoreCase = true) }

    return FighterRates(
        finishRate = (koWins + submissionWins).toFloat() / totalWins,
        koTkoRate = koWins.toFloat() / totalWins,
        submissionRate = submissionWins.toFloat() / totalWins
    )
}