package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FighterInteractionDto
import com.berkekucuk.mmaapp.data.remote.dto.FighterInteractionInsertDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class FighterInteractionSupabaseAPI(
    private val client: SupabaseClient
) : FighterInteractionRemoteDataSource {

    override suspend fun fetchInteractions(userId: String): List<FighterInteractionDto> {
        return client.from("fighter_interaction_view").select {
            filter {
                eq("user_id", userId)
            }
        }.decodeList<FighterInteractionDto>()
    }

    override suspend fun submitInteraction(
        userId: String,
        fighterId: String,
        interactionType: String
    ): FighterInteractionDto {
        val request = FighterInteractionInsertDto(userId, fighterId, interactionType)

        return client.from("user_fighter_interactions").insert(request) {
            select()
        }.decodeSingle<FighterInteractionDto>()
    }

    override suspend fun removeInteraction(interactionId: String) {
        client.from("user_fighter_interactions").delete {
            filter {
                eq("id", interactionId)
            }
        }
    }
}
