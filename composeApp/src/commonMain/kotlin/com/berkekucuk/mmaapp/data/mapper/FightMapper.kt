package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import com.berkekucuk.mmaapp.domain.model.Fight
import com.berkekucuk.mmaapp.graphql.GetEventsQuery

fun GetEventsQuery.Fight.toDto(): FightDto {
    return FightDto(
        fightId = fight_id,
        eventId = event_id,
        methodType = method_type,
        methodDetail = method_detail,
        roundSummary = round_summary,
        boutType = bout_type,
        weightClassLbs = weight_class_lbs,
        weightClassId = weight_class_id,
        roundsFormat = rounds_format,
        fightOrder = fight_order,
        participants = participants?.mapNotNull { it?.toDto() }
    )
}

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
