package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.FighterRecord
import com.berkekucuk.mmaapp.domain.model.Measurement
import kotlinx.serialization.json.Json

private val jsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}

fun FighterDto.toEntity(): FighterEntity {
    return FighterEntity(
        fighterId = this.fighterId,
        name = this.name,
        nickname = this.nickname,
        weightClassId = this.weightClassId,
        record = record?.let { jsonParser.encodeToString(it) },
        height = height?.let { jsonParser.encodeToString(it) },
        reach = reach?.let { jsonParser.encodeToString(it) },
        style = this.style,
        dateOfBirth = this.dateOfBirth,
        born = this.born,
        fightingOutOf = this.fightingOutOf,
        countryCode = this.countryCode,
        imageUrl = this.imageUrl,
        )
}

fun FighterEntity.toDomain(): Fighter {
    return Fighter(
        fighterId = this.fighterId,
        name = this.name ?: "N/A",
        nickname = this.nickname ?: "N/A",
        weightClassId = this.weightClassId ?: "N/A",
        record = this.record?.let { jsonParser.decodeFromString<FighterRecord>(it) },
        height = this.height?.let { jsonParser.decodeFromString<Measurement>(it) },
        reach = this.reach?.let { jsonParser.decodeFromString<Measurement>(it) },
        style = this.style ?: "N/A",
        dateOfBirth = this.dateOfBirth ?: "N/A",
        born = this.born ?: "N/A",
        fightingOutOf = this.fightingOutOf ?: "N/A",
        countryCode = this.countryCode ?: "N/A",
        imageUrl = this.imageUrl ?: ""
    )
}