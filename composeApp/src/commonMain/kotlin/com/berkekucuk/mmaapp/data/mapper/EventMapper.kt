package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.enums.EventStatus

fun EventDto.toEntity(): EventEntity {
    return EventEntity(
        eventId = this.eventId,
        name = this.name,
        status = this.status,
        datetimeUtc = this.datetimeUtc,
        venue = this.venue,
        location = this.location,
        eventYear = this.eventYear,
        fights = this.fights
    )
}

fun EventEntity.toDomain(): Event {
    return Event(
        eventId = this.eventId,
        name = this.name ?: "Unannounced Event",
        status = parseEventStatus(this.status),
        datetimeUtc = this.datetimeUtc ,
        venue = this.venue ?: "N/A",
        location = this.location ?: "N/A",
        eventYear = this.eventYear,
        fights = fights?.map { it.toDomain() } ?: emptyList()
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