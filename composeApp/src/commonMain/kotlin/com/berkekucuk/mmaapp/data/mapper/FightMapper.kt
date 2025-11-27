package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.remote.dto.FightDto

fun FightDto.toEntity(): FightEntity {
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


