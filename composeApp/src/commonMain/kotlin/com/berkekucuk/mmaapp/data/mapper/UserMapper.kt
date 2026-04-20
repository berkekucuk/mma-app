package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import com.berkekucuk.mmaapp.data.local.entity.UserFighterFavoriteEntity
import com.berkekucuk.mmaapp.data.local.entity.UserWithFavoriteFighters
import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.Measurement
import com.berkekucuk.mmaapp.domain.model.Record
import com.berkekucuk.mmaapp.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
    )
}

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

fun UserWithFavoriteFighters.toDomain(): User {
    return User(
        id = user.id,
        username = user.username,
        fullName = user.fullName,
        avatarUrl = user.avatarUrl,
        favoriteFighters = favoriteFighters.map { it.toDomain() },
    )
}
