package com.berkekucuk.mmaapp.presentation.screens.fight_detail

import com.berkekucuk.mmaapp.domain.enums.Result
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.Participant

data class FighterRates(
    val winRate: Float,
    val koTkoRate: Float,
    val submissionRate: Float,
)

fun Fighter.calculateRates(): FighterRates {
    val total = record.wins + record.losses + record.draws
    val winRate = if (total == 0) 0f else record.wins.toFloat() / total

    val wins = fights.filter { fight ->
        fight.participants.any { p ->
            p.fighter.fighterId == this.fighterId && p.result == Result.WIN
        }
    }
    val totalWins = wins.size
    if (totalWins == 0) return FighterRates(winRate = winRate, koTkoRate = 0f, submissionRate = 0f)

    val koWins = wins.count { it.methodType.contains("ko", ignoreCase = true) }
    val submissionWins = wins.count { it.methodType.contains("sub", ignoreCase = true) }

    return FighterRates(
        winRate = winRate,
        koTkoRate = koWins.toFloat() / totalWins,
        submissionRate = submissionWins.toFloat() / totalWins
    )
}

const val RADAR_AXIS_COUNT = 6
private const val MIN_HEIGHT = 155f
private const val MAX_HEIGHT = 200f
private const val MIN_REACH = 155f
private const val MAX_REACH = 210f
private const val MIN_ODDS = -600f
private const val MAX_ODDS = 600f

fun buildRadarValues(participant: Participant?, rates: FighterRates): List<Float> {
    val fighter = participant?.fighter
    return listOf(
        normalizeValue(fighter?.height?.metric?.toFloat(), MIN_HEIGHT, MAX_HEIGHT),
        normalizeValue(fighter?.reach?.metric?.toFloat(), MIN_REACH, MAX_REACH),
        normalizeOdds(participant?.oddsValue),
        rates.winRate,
        rates.koTkoRate,
        rates.submissionRate,
    )
}

private fun normalizeValue(value: Float?, min: Float, max: Float): Float {
    if (value == null) return 0f
    return ((value - min) / (max - min)).coerceIn(0f, 1f)
}

private fun normalizeOdds(odds: Int?): Float {
    if (odds == null) return 0.5f
    val normalized = ((-odds.toFloat()) - MIN_ODDS) / (MAX_ODDS - MIN_ODDS)
    return normalized.coerceIn(0f, 1f)
}