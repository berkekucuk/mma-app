package com.berkekucuk.mmaapp.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Event @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val name: String,
    val status: EventStatus,
    val date: Instant,
    val venue: String,
    val location: String
)

enum class EventStatus {
    UPCOMING,
    LIVE,
    COMPLETED,
    CANCELLED,
    UNKNOWN
}