package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.EventStatus
import kotlin.time.Instant

fun EventDto.toEntity(): EventEntity {
    return EventEntity(
        eventId = this.eventId,
        name = this.name,
        status = this.status,
        datetimeUtc = this.datetimeUtc,
        venue = this.venue,
        location = this.location
    )
}

fun EventEntity.toDomain(): Event {
    return Event(
        id = this.eventId,
        name = this.name ?: "N/A",
        status = parseEventStatus(this.status),
        date = this.datetimeUtc ?: Instant.fromEpochMilliseconds(0),
        venue = this.venue ?: "N/A",
        location = this.location ?: "N/A"
    )
}

private fun parseEventStatus(status: String?): EventStatus {
    return when (status?.lowercase()) {
        "upcoming" -> EventStatus.UPCOMING
        "live" -> EventStatus.LIVE
        "completed" -> EventStatus.COMPLETED
        "cancelled" -> EventStatus.CANCELLED
        else -> EventStatus.UNKNOWN
    }
}