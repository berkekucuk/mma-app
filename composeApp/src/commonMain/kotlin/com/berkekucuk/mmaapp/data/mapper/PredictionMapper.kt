package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.PredictionEntity
import com.berkekucuk.mmaapp.data.remote.dto.PredictionDto

fun PredictionDto.toEntity(): PredictionEntity =
    PredictionEntity(
        fightId = fightId,
        userId = userId,
        predictedWinnerId = predictedWinnerId,
        lockedOdds = lockedOdds
    )