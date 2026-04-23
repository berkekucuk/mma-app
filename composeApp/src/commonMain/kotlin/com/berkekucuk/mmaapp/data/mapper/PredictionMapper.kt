package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.PredictionEntity
import com.berkekucuk.mmaapp.data.remote.dto.PredictionDto
import kotlin.time.Instant

import com.berkekucuk.mmaapp.domain.model.Prediction

fun PredictionDto.toEntity(): PredictionEntity =
    PredictionEntity(
        predictionId = predictionId,
        fightId = fight?.fightId ?: "",
        userId = userId,
        predictedWinnerId = predictedWinnerId ?: "",
        pointsEarned = pointsEarned ?: 0,
        isCorrect = isCorrect,
        lockedOdds = lockedOdds,
        createdAt = createdAt ?: Instant.fromEpochMilliseconds(0),
        fight = fight
    )

fun PredictionEntity.toDomain(): Prediction =
    Prediction(
        predictionId = predictionId,
        fightId = fightId,
        userId = userId,
        predictedWinnerId = predictedWinnerId,
        pointsEarned = pointsEarned,
        isCorrect = isCorrect,
        lockedOdds = lockedOdds,
        createdAt = createdAt,
        fight = fight?.toDomain()
    )