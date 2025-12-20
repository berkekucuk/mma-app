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
    val fights: List<Fight>
) {
    val mainEvent: Fight?
        get() = fights.firstOrNull()
}


