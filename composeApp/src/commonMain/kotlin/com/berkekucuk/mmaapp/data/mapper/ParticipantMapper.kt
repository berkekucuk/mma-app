package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDto
import com.berkekucuk.mmaapp.domain.model.FighterRecord
import com.berkekucuk.mmaapp.domain.model.Participant
import kotlinx.serialization.json.Json

private val jsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}

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

fun ParticipantEntity.toDomain(): Participant {
    return Participant(
        id = this.id,
        fightId = this.fightId,
        fighterId = this.fighterId,
        oddsValue = this.oddsValue,
        oddsLabel = this.oddsLabel ?: "N/A",
        result = this.result ?: "N/A",
        recordAfterFight = this.recordAfterFight?.let { jsonParser.decodeFromString<FighterRecord>(it) },
        isWinner = this.isWinner
    )
}