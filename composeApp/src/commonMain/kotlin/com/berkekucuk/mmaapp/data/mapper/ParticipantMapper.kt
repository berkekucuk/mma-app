package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDto
import com.berkekucuk.mmaapp.domain.enums.Result
import com.berkekucuk.mmaapp.domain.enums.WeightClass
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.Measurement
import com.berkekucuk.mmaapp.domain.model.Participant
import com.berkekucuk.mmaapp.domain.model.Record

fun ParticipantDto.toDomain(): Participant {
    return Participant(
        oddsValue = oddsValue,
        oddsLabel = oddsLabel ?: "",
        result = parseResult(result),
        recordAfterFight = recordAfterFight?.toDomain(),
        isRedCorner = isRedCorner ?: false,
        fighter = fighter?.toDomain() ?: createUnknownFighter()
    )
}

private fun createUnknownFighter(): Fighter {
    return Fighter(
        fighterId = "",
        name = "Unknown Fighter",
        nickname = "",
        record = Record.EMPTY,
        dateOfBirth = "",
        height = Measurement.EMPTY,
        reach = Measurement.EMPTY,
        weightClassId = WeightClass.UNKNOWN,
        born = "",
        fightingOutOf = "",
        style = "",
        countryCode = "",
        imageUrl = ""
    )
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

