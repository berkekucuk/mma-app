package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.dto.EventDto
import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.EventStatus

// DTO -> Entity
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

// Entity -> Domain
fun EventEntity.toDomain(): Event {
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
        "upcoming" -> EventStatus.UPCOMING
        "live" -> EventStatus.LIVE
        "completed", "finished" -> EventStatus.COMPLETED
        "cancelled" -> EventStatus.CANCELLED
        else -> EventStatus.UNKNOWN
    }
}