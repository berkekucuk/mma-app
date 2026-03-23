package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import com.berkekucuk.mmaapp.domain.model.Fight

fun FightDto.toDomain(): Fight {
    return Fight(
        fightId = fightId,
        eventId = eventId,
        eventName = eventName,
        eventDate = eventDate,
        methodType = methodType ?: "",
        methodDetail = methodDetail ?: "",
        roundSummary = roundSummary ?: "",
        boutType = boutType ?: "",
        weightClassLbs = weightClassLbs,
        weightClassId = weightClassId ?: "",
        roundsFormat = roundsFormat ?: "",
        fightOrder = fightOrder ?: 0,
        participants = participants?.map { it.toDomain() } ?: emptyList()
    )
}
