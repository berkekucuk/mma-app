package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class Event(
    val id: String,
    val name: String,
    val status: EventStatus,
    val datetimeUtc: Instant?,
    val venue: String,
    val location: String,
    val fights: List<Fight>
) {
    val mainEvent: Fight?
        get() = fights.firstOrNull()
}

@Immutable
data class Fight(
    val method: String,
    val methodDetail: String,
    val roundSummary: String,
    val weightClass: WeightClass,
    val participants: List<Participant>
) {
    val redCorner: Participant?
        get() = participants.find { it.isRedCorner == true }

    val blueCorner: Participant?
        get() = participants.find { it.isRedCorner == false }
}

@Immutable
data class Participant(
    val name: String,
    val record: String,
    val imageUrl: String?,
    val countryCode: String?,
    val isRedCorner: Boolean?,
    val result: Result,
    val recordAfterFight: String?
)

enum class EventStatus {
    UPCOMING,
    LIVE,
    COMPLETED,
    CANCELLED,
    UNKNOWN
}

enum class Result{
    WIN,
    LOSS,
    DRAW,
    NO_CONTEST,
    PENDING,
    CANCELLED,
    FIZZLED,
    UNKNOWN
}

enum class WeightClass{
    STRAWWEIGHT,
    FLYWEIGHT,
    BANTAMWEIGHT,
    FEATHERWEIGHT,
    LIGHTWEIGHT,
    WELTERWEIGHT,
    MIDDLEWEIGHT,
    LIGHTHEAVYWEIGHT,
    HEAVYWEIGHT,
    CATCHWEIGHT,
    UNKNOWN
}
