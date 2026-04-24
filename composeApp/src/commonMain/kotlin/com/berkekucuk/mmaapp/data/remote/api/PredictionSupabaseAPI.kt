package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.PredictionDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class PredictionSupabaseAPI(
    private val client: SupabaseClient
) : PredictionRemoteDataSource {

    override suspend fun fetchPredictions(userId: String): List<PredictionDto> {
        return client.from("prediction_view").select {
            filter {
                eq("user_id", userId)
            }
        }.decodeList<PredictionDto>()
    }

    override suspend fun submitPrediction(
        userId: String,
        fightId: String,
        predictedWinnerId: String,
        lockedOdds: Int
    ): PredictionDto {
        return client.from("predictions").insert(
            mapOf(
                "user_id" to userId,
                "fight_id" to fightId,
                "predicted_winner_id" to predictedWinnerId,
                "locked_odds" to lockedOdds
            )
        ) {
            select()
        }.decodeSingle<PredictionDto>()
    }
}
