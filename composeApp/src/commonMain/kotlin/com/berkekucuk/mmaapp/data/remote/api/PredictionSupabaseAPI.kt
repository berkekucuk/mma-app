package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.PredictionDto
import com.berkekucuk.mmaapp.data.remote.dto.PredictionInsertDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class PredictionSupabaseAPI(
    private val client: SupabaseClient
) : PredictionRemoteDataSource {

    override suspend fun fetchPredictions(userId: String): List<PredictionDto> {
        return client.from("prediction_view").select {
            filter {
                eq("user_id", userId)
            }
            order(column = "created_at", order = Order.DESCENDING)
            limit(15)
        }.decodeList<PredictionDto>()
    }

    override suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int): PredictionDto {
        val payload = PredictionInsertDto(
            userId = userId,
            fightId = fightId,
            predictedWinnerId = predictedWinnerId,
            lockedOdds = lockedOdds
        )

        client.from("user_predictions").insert(payload)

        return client.from("prediction_view").select {
            filter {
                and {
                    eq("user_id", userId)
                    eq("fight_id", fightId)
                }
            }
        }.decodeSingle<PredictionDto>()
    }
}
