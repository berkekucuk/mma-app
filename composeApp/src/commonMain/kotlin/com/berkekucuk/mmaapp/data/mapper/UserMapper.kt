package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import com.berkekucuk.mmaapp.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        userFighterFavorites = userFighterFavorites,
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        favoriteFighters = userFighterFavorites.map { it.toDomain() },
    )
}
