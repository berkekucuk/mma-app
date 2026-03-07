package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.domain.enums.WeightClass
import com.berkekucuk.mmaapp.domain.model.Fighter
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
        weightClassId = WeightClass.fromId(weightClassId),
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
        fights = fights,
    )
}

fun FighterEntity.toDomain(): Fighter {
    return Fighter(
        fighterId = fighterId,
        name = name ?: "",
        nickname = nickname,
        imageUrl = imageUrl ?: "",
        record = record?.toDomain() ?: Record.EMPTY,
        height = height?.toDomain() ?: Measurement.EMPTY,
        reach = reach?.toDomain() ?: Measurement.EMPTY,
        weightClassId = WeightClass.fromId(weightClassId),
        dateOfBirth = dateOfBirth ?: "",
        born = born,
        fightingOutOf = fightingOutOf,
        countryCode = countryCode ?: "",
        fights = fights?.map { it.toDomain() } ?: emptyList(),
    )
}