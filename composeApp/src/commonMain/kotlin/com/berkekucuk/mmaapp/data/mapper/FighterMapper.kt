package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.domain.enums.WeightClass
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.Measurement
import com.berkekucuk.mmaapp.domain.model.Record

fun FighterDto.toDomain(): Fighter {
    return Fighter(
        fighterId = fighterId,
        name = name ?: "",
        nickname = nickname ?: "",
        record = record?.toDomain() ?: Record.EMPTY,
        dateOfBirth = dateOfBirth ?: "",
        height = height?.toDomain() ?: Measurement.EMPTY,
        reach = reach?.toDomain() ?: Measurement.EMPTY,
        weightClassId = parseWeightClass(weightClassId),
        born = born ?: "",
        fightingOutOf = fightingOutOf ?: "",
        style = style ?: "",
        countryCode = countryCode ?: "",
        imageUrl = imageUrl ?: ""
    )
}

private fun parseWeightClass(weightClassId: String?): WeightClass {
    return when (weightClassId?.lowercase()) {
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
