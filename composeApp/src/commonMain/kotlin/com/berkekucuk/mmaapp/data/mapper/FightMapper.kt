package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.remote.dto.FightCardDto

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


