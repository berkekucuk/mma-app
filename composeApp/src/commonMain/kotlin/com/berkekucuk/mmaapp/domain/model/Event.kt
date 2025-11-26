package com.berkekucuk.mmaapp.domain.model

import kotlin.time.Instant

data class Event (
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