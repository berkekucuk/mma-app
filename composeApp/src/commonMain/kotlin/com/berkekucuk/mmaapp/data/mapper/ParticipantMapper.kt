package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDto
import com.berkekucuk.mmaapp.domain.model.FighterRecord
import com.berkekucuk.mmaapp.domain.model.Participant
import com.berkekucuk.mmaapp.domain.model.Result
import kotlinx.serialization.json.Json

private val jsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}

fun ParticipantDto.toEntity(): ParticipantEntity {
    return ParticipantEntity(
        id = this.id,
        fightId = this.fightId,
        fighterId = this.fighterId,
        oddsValue = this.oddsValue,
        oddsLabel = this.oddsLabel,
        result = this.result,
        recordAfterFight = this.recordAfterFight?.let { jsonParser.encodeToString(it) },
        isRedCorner = this.isRedCorner
    )
}

fun ParticipantEntity.toDomain(): Participant {
    return Participant(
        id = this.id,
        fightId = this.fightId,
        fighterId = this.fighterId,
        oddsValue = this.oddsValue,
        oddsLabel = this.oddsLabel ?: "N/A",
        result = parseResult(this.result),
        recordAfterFight = this.recordAfterFight?.let { jsonParser.decodeFromString<FighterRecord>(it) },
        isRedCorner = this.isRedCorner ?: false
    )
}

fun parseResult(result: String?): Result {
    return when(result?.lowercase()){
        "win" -> Result.WIN
        "loss" -> Result.LOSS
        "draw" -> Result.DRAW
        "no_contest" -> Result.NO_CONTEST
        "pending" -> Result.PENDING
        "cancelled" -> Result.CANCELLED
        "fizzled" -> Result.FIZZLED
        else -> Result.PENDING
    }
}
