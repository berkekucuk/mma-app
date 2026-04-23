package com.berkekucuk.mmaapp.data.remote.api

interface PredictionRemoteDataSource {
    suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int)
}
