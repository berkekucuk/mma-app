package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Prediction
import kotlinx.coroutines.flow.Flow

interface PredictionRepository {
    fun getPredictedWinnerId(fightId: String, userId: String): Flow<String?>
    fun getPredictions(userId: String): Flow<List<Prediction>>
    suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int): Result<Unit>
    suspend fun syncPredictions(userId: String): Result<Unit>
}
