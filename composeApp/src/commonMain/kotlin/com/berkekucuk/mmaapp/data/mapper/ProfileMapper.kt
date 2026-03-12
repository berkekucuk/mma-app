package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.ProfileEntity
import com.berkekucuk.mmaapp.data.remote.dto.ProfileDto
import com.berkekucuk.mmaapp.domain.model.Profile

fun ProfileDto.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        favoriteFighterId = favoriteFighterId,
    )
}

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        favoriteFighterId = favoriteFighterId,
    )
}
