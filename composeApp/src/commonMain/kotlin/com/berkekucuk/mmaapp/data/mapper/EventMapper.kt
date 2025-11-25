package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.dto.EventDto
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.EventStatus
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun EventDto.toDomain(): Event {
    return Event(
        id = this.eventId,
        name = this.name,
        status = parseEventStatus(this.status),
        date = this.datetimeUtc,
        venue = this.venue ?: "Unknown Venue",
        location = this.location ?: "Unknown Location"
    )
}

private fun parseEventStatus(status: String?): EventStatus {
    return when (status?.lowercase()) {
        "Upcoming" -> EventStatus.UPCOMING
        "live" -> EventStatus.LIVE
        "Completed" -> EventStatus.COMPLETED
        "Cancelled" -> EventStatus.CANCELLED
        else -> EventStatus.UNKNOWN
    }
}