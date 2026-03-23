package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import com.berkekucuk.mmaapp.domain.enums.EventStatus
import kotlin.time.Instant

@Immutable
data class Event(
    val eventId: String,
    val name: String,
    val status: EventStatus,
    val datetimeUtc: Instant?,
    val venue: String,
    val location: String,
    val eventYear: Int?,
    val fights: List<Fight>
) {
    val mainFight: Fight?
        get() = fights.firstOrNull()

    val mainCardFights: List<Fight>
        get() = fights.filter {
            it.boutType.contains("Main Card", ignoreCase = true) ||
            it.boutType.contains("Main Event", ignoreCase = true) ||
            it.boutType.contains("Co-Main", ignoreCase = true)
        }.sortedByDescending { it.fightOrder }

    val prelimFights: List<Fight>
        get() = fights.filter {
            it.boutType.contains("Prelim", ignoreCase = true)
        }.sortedByDescending { it.fightOrder }
}


