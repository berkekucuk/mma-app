package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.data.remote.dto.FighterHistoryDto
import com.berkekucuk.mmaapp.domain.enums.WeightClass
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.FighterHistory
import com.berkekucuk.mmaapp.domain.model.Measurement
import com.berkekucuk.mmaapp.domain.model.Record
import com.berkekucuk.mmaapp.graphql.GetEventsQuery

fun GetEventsQuery.Fighters.toDto(): FighterDto {
    return FighterDto(
        fighterId = fighter_id,
        name = name,
        nickname = nickname,
        imageUrl = image_url,
        record = record?.toDto(),
        height = height?.toDto(),
        reach = reach?.toDto(),
        weightClassId = weight_class_id,
        dateOfBirth = date_of_birth,
        born = born,
        fightingOutOf = fighting_out_of,
        countryCode = country_code
    )
}

fun FighterDto.toDomain(): Fighter {
    return Fighter(
        fighterId = fighterId,
        name = name ?: "",
        nickname = nickname ?: "",
        imageUrl = imageUrl ?: "",
        record = record?.toDomain() ?: Record.EMPTY,
        height = height?.toDomain() ?: Measurement.EMPTY,
        reach = reach?.toDomain() ?: Measurement.EMPTY,
        weightClassId = parseWeightClass(weightClassId),
        dateOfBirth = dateOfBirth ?: "",
        born = born ?: "",
        fightingOutOf = fightingOutOf ?: "",
        countryCode = countryCode ?: ""
    )
}

fun FighterDto.toEntity(): FighterEntity {
    return FighterEntity(
        fighterId = fighterId,
        name = name,
        nickname = nickname,
        imageUrl = imageUrl,
        record = record,
        height = height,
        reach = reach,
        weightClassId = weightClassId,
        dateOfBirth = dateOfBirth,
        born = born,
        fightingOutOf = fightingOutOf,
        countryCode = countryCode,
        fighterHistory = fighterHistory,
    )
}

fun FighterEntity.toDomain(): Fighter {
    return Fighter(
        fighterId = fighterId,
        name = name ?: "",
        nickname = nickname ?: "",
        imageUrl = imageUrl ?: "",
        record = record?.toDomain() ?: Record.EMPTY,
        height = height?.toDomain() ?: Measurement.EMPTY,
        reach = reach?.toDomain() ?: Measurement.EMPTY,
        weightClassId = parseWeightClass(weightClassId),
        dateOfBirth = dateOfBirth ?: "",
        born = born ?: "",
        fightingOutOf = fightingOutOf ?: "",
        countryCode = countryCode ?: "",
        fighterHistory = fighterHistory?.map { it.toDomain() } ?: emptyList(),
    )
}

fun FighterHistoryDto.toDomain(): FighterHistory {
    return FighterHistory(
        eventName = eventName ?: "",
        eventDate = eventDate,
        methodType = methodType ?: "",
        methodDetail = methodDetail ?: "",
        roundSummary = roundSummary ?: "",
        boutType = boutType ?: "",
        weightClassLbs = weightClassLbs,
        roundsFormat = roundsFormat ?: "",
        weightClassId = weightClassId ?: "",
        oddsValue = oddsValue,
        oddsLabel = oddsLabel ?: "",
        result = result ?: "",
        recordAfterFight = recordAfterFight ?: "",
        isRedCorner = isRedCorner ?: false,
        oppFighterId = oppFighterId ?: "",
        oppName = oppName ?: "",
        oppImageUrl = oppImageUrl ?: "",
        oppRecord = oppRecord?.toDomain() ?: Record.EMPTY,
        oppFightingOutOf = oppFightingOutOf ?: "",
        oppHeight = oppHeight?.toDomain() ?: Measurement.EMPTY,
        oppReach = oppReach?.toDomain() ?: Measurement.EMPTY,
        oppDateOfBirth = oppDateOfBirth ?: "",
        oppCountryCode = oppCountryCode ?: "",
        oppOddsValue = oppOddsValue,
        oppOddsLabel = oppOddsLabel ?: "",
        oppResult = oppResult ?: "",
        oppRecordAfterFight = oppRecordAfterFight ?: "",
        oppIsRedCorner = oppIsRedCorner ?: false,
    )
}

private fun parseWeightClass(weightClassId: String?): WeightClass {
    return when (weightClassId?.lowercase()) {
        "sw" -> WeightClass.STRAWWEIGHT
        "flw" -> WeightClass.FLYWEIGHT
        "w_flw" -> WeightClass.WOMENS_FLYWEIGHT
        "bw" -> WeightClass.BANTAMWEIGHT
        "w_bw" -> WeightClass.WOMENS_BANTAMWEIGHT
        "fw" -> WeightClass.FEATHERWEIGHT
        "w_fw" -> WeightClass.WOMENS_FEATHERWEIGHT
        "lw" -> WeightClass.LIGHTWEIGHT
        "ww" -> WeightClass.WELTERWEIGHT
        "mw" -> WeightClass.MIDDLEWEIGHT
        "lhw" -> WeightClass.LIGHTHEAVYWEIGHT
        "hw" -> WeightClass.HEAVYWEIGHT
        "cw" -> WeightClass.CATCHWEIGHT
        else -> WeightClass.UNKNOWN
    }
}