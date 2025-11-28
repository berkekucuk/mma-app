package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.remote.dto.FightCardDto
import com.berkekucuk.mmaapp.domain.model.Fight

fun FightCardDto.toEntity(): FightEntity {
    return FightEntity(
        fightId = this.fightId,
        eventId = this.eventId,
        methodType = this.methodType,
        methodDetail = this.methodDetail,
        roundSummary = this.roundSummary,
        boutType = this.boutType,
        weightClassLbs = this.weightClassLbs,
        roundsFormat = this.roundsFormat,
        fightOrder = this.fightOrder
    )
}

fun FightEntity.toDomain(): Fight {
    return Fight(
        fightId = fightId,
        eventId = eventId,
        methodType = methodType ?: "N/A",
        methodDetail = methodDetail ?: "N/A",
        roundSummary = roundSummary ?: "N/A",
        boutType = boutType ?: "N/A",
        weightClassLbs = weightClassLbs,
        roundsFormat = roundsFormat ?: "N/A",
        fightOrder = fightOrder ?: 0
    )
}


