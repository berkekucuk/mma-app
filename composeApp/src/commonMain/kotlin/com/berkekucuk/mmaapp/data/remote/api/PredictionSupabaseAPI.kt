package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.PredictionDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class PredictionSupabaseAPI(
    private val client: SupabaseClient
) : PredictionRemoteDataSource {

    override suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int) {
        val payload = PredictionDto(
            userId = userId,
            fightId = fightId,
            predictedWinnerId = predictedWinnerId,
            lockedOdds = lockedOdds
        )

        client.from("user_predictions").insert(payload)
    }
}
