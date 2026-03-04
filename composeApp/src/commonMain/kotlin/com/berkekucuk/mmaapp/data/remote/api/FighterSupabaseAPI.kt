package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class FighterSupabaseAPI(
    private val client: SupabaseClient
) : FighterRemoteDataSource {

    override suspend fun fetchFighterById(id: String): FighterDto {
        return client.from("fighter_match_history").select {
            filter {
                eq("fighter_id", id)
            }
        }.decodeSingle<FighterDto>()
    }
}