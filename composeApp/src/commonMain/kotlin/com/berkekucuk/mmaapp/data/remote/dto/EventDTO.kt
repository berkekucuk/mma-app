package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EventDTO(
    @SerialName("event_id") val eventId: String,
    val name: String? = null,
    val status: String? = null,
    @SerialName("datetime_utc") val datetimeUtc: Instant? = null,
    val venue: String? = null,
    val location: String? = null,
    val fights: List<FightDTO> = emptyList()
)