package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.UserEntity
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.data.remote.dto.RankedFighterDto
import com.berkekucuk.mmaapp.data.remote.dto.UserDto
import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.RankedFighter
import com.berkekucuk.mmaapp.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        favoriteFighters = favoriteFighters ?: emptyList(),
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        fullName = fullName,
        avatarUrl = avatarUrl,
        favoriteFighters = favoriteFighters
            .map { RankedFighter(rankNumber = it.rankNumber, fighter = it.fighter?.toDomain()) }
    )
}

fun Fighter.toFavoriteDto(): RankedFighterDto {
    return RankedFighterDto(
        rankNumber = 0,
        fighter = FighterDto(
            fighterId = fighterId,
            name = name,
            imageUrl = imageUrl,
            record = record.toDto(),
            countryCode = countryCode,
        )
    )
}