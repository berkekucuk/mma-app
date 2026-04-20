package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.FightNotificationEntity
import com.berkekucuk.mmaapp.data.remote.dto.FightNotificationDto

fun FightNotificationDto.toEntity(): FightNotificationEntity =
    FightNotificationEntity(fightId = fightId, userId = userId)
