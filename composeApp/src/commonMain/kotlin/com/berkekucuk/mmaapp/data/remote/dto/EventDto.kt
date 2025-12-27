package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EventDto(
    @SerialName("event_id") val eventId: String,
    val name: String? = null,
    val status: String? = null,
    @SerialName("datetime_utc") val datetimeUtc: Instant? = null,
    val venue: String? = null,
    val location: String? = null,
    @SerialName("event_year") val eventYear: Int? = null,
    val fights: List<FightDto>? = null
)