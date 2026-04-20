package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.UserFighterFavoriteEntity
import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.Measurement
import com.berkekucuk.mmaapp.domain.model.Record

fun UserDto.toFavoriteEntities(): List<UserFighterFavoriteEntity> {
    return userFighterFavorites.map {
        UserFighterFavoriteEntity(
            userId = id,
            fighterId = it.fighterId,
            name = it.name,
            imageUrl = it.imageUrl,
            record = it.record,
        )
    }
}

fun UserFighterFavoriteEntity.toDomain(): Fighter {
    return Fighter(
        fighterId = fighterId,
        name = name ?: "",
        nickname = null,
        imageUrl = imageUrl ?: "",
        record = record?.toDomain() ?: Record.EMPTY,
        height = Measurement.EMPTY,
        reach = Measurement.EMPTY,
        weightClassId = "",
        dateOfBirth = "",
        born = null,
        fightingOutOf = null,
        countryCode = "",
        fights = emptyList()
    )
}

fun Fighter.toFavoriteEntity(userId: String): UserFighterFavoriteEntity {
    return UserFighterFavoriteEntity(
        userId = userId,
        fighterId = fighterId,
        name = name,
        imageUrl = imageUrl,
        record = record.toDto()
    )
}
