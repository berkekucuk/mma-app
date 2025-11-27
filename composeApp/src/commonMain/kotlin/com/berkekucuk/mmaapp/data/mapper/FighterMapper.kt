package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto

fun FighterDto.toEntity(): FighterEntity {
    return FighterEntity(
        fighterId = this.fighterId,
        name = this.name,
        nickname = this.nickname,
        imageUrl = this.imageUrl,
        record = this.record?.toString(),
        height = this.height?.toString(),
        reach = this.reach?.toString(),
        weightClassName = this.weightClassName,
        dateOfBirth = this.dateOfBirth,
        born = this.born,
        fightingOutOf = this.fightingOutOf,
        style = this.style,
        countryCode = this.countryCode
    )
}