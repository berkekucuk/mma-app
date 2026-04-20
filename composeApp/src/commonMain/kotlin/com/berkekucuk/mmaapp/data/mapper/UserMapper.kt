package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import com.berkekucuk.mmaapp.data.local.entity.UserWithFavoriteFighters
import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import com.berkekucuk.mmaapp.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
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