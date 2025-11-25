package com.berkekucuk.mmaapp.data.dto

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class EventDto @OptIn(ExperimentalTime::class) constructor(
    @SerialName("event_id")
    val eventId: String,

    @SerialName("name")
    val name: String,

    @SerialName("status")
    val status: String?,

    @SerialName("datetime_utc")
    val datetimeUtc: Instant, // Supabase TIMESTAMPTZ -> Kotlin Instant

    @SerialName("venue")
    val venue: String?,

    @SerialName("location")
    val location: String?
)