package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Interaction(
    val id: String,
    val userId: String,
    val fighterId: String,
    val interactionType: String,
    val rankNumber: Int?,
    val fighter: Fighter?
)

fun Interaction.toRankedFighter(): RankedFighter {
    return RankedFighter(
        rankNumber = rankNumber ?: 0,
        fighter = fighter
    )
}