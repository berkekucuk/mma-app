package com.berkekucuk.mmaapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface PredictionRepository {
    fun getPredictedWinnerId(fightId: String, userId: String): Flow<String?>
    suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int): Result<Unit>
}
