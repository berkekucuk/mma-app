package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import com.berkekucuk.mmaapp.data.remote.dto.EventDTO
import com.berkekucuk.mmaapp.data.remote.dto.FightDTO
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDTO
import com.berkekucuk.mmaapp.domain.model.Event
import com.berkekucuk.mmaapp.domain.model.EventStatus
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.domain.model.Participant
import com.berkekucuk.mmaapp.domain.model.Result
import com.berkekucuk.mmaapp.domain.model.WeightClass

fun EventDTO.toEntity(): EventEntity {
    return EventEntity(
        eventId = this.eventId,
        name = this.name,
        status = this.status,
        datetimeUtc = this.datetimeUtc,
        venue = this.venue,
        location = this.location,
        fights = this.fights
    )
}

fun EventEntity.toDomain(): Event {
    return Event(
        id = this.eventId,
        name = this.name ?: "Unannounced Event",
        status = parseEventStatus(this.status),
        datetimeUtc = this.datetimeUtc,
        venue = this.venue ?: "N/A",
        location = this.location ?: "N/A",
        fights = this.fights.map { it.toDomain() }
    )
}

fun FightDTO.toDomain(): Fight {
    return Fight(
        method = this.methodType ?: "",
        methodDetail = this.methodDetail ?: "",
        roundSummary = this.roundSummary ?: "",
        weightClass = parseWeightClass(this.weightClassId),
        participants = this.participants.map { it.toDomain() }
    )
}

fun ParticipantDTO.toDomain(): Participant {
    return Participant(
        name = this.fighter?.name ?: "Unknown Fighter",
        record = this.fighter?.record?.let {
            "${it.wins}-${it.losses}-${it.draws}"
        } ?: "0-0-0",
        imageUrl = this.fighter?.imageUrl,
        countryCode = this.fighter?.countryCode,
        isRedCorner = this.isRedCorner,
        result = parseResult(this.result),
        recordAfterFight = this.recordAfterFight?.let {
            "${it.wins}-${it.losses}-${it.draws}"
        }
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

private fun parseResult(result: String?): Result {
    return when (result?.lowercase()) {
        "win" -> Result.WIN
        "loss" -> Result.LOSS
        "draw" -> Result.DRAW
        "no_contest" -> Result.NO_CONTEST
        "pending" -> Result.PENDING
        "cancelled" -> Result.CANCELLED
        "fizzled" -> Result.FIZZLED
        else -> Result.UNKNOWN
    }
}

private fun parseWeightClass(weightClassId: String?): WeightClass {
    return when (weightClassId?.lowercase()){
        "sw" -> WeightClass.STRAWWEIGHT
        "flw" -> WeightClass.FLYWEIGHT
        "bw" -> WeightClass.BANTAMWEIGHT
        "fw" -> WeightClass.FEATHERWEIGHT
        "lw" -> WeightClass.LIGHTWEIGHT
        "ww" -> WeightClass.WELTERWEIGHT
        "mw" -> WeightClass.MIDDLEWEIGHT
        "lhw" -> WeightClass.LIGHTHEAVYWEIGHT
        "hw" -> WeightClass.HEAVYWEIGHT
        "cw" -> WeightClass.CATCHWEIGHT
        else -> WeightClass.UNKNOWN
    }
}