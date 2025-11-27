package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDto

fun ParticipantDto.toEntity(): ParticipantEntity {

    val isWin = this.result?.equals("win", ignoreCase = true) == true

    return ParticipantEntity(
        id = this.id,
        fightId = this.fightId,
        fighterId = this.fighterId,
        oddsValue = this.oddsValue,
        oddsLabel = this.oddsLabel,
        result = this.result,
        recordAfterFight = this.recordAfterFight?.toString(),
        isWinner = isWin
    )
}