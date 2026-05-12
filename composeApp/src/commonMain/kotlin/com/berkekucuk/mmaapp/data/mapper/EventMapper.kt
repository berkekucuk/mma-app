package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import com.berkekucuk.mmaapp.data.local.relation.EventWithFightsRelation
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
        eventYear = this.eventYear
    )
}

fun EventWithFightsRelation.toDomain(): Event {
    return Event(
        eventId = event.eventId,
        name = event.name,
        status = EventStatus.fromString(event.status),
        datetimeUtc = event.datetimeUtc,
        venue = event.venue,
        location = event.location,
        eventYear = event.eventYear,
        fights = fights
            .filter { !it.boutType.equals("cancelled", ignoreCase = true) }
            .map { it.toDomain() }
            .sortedByDescending { it.fightOrder }
    )
}