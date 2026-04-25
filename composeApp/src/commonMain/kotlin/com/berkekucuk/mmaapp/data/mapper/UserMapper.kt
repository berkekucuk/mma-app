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
        totalPoints = totalPoints ?: 0,
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        totalPoints = totalPoints,
    )
}