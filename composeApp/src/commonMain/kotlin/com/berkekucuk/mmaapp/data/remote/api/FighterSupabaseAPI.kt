package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

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

    override suspend fun searchFighters(query: String): List<FighterDto> {
        return client.from("fighters").select(
            columns = Columns.raw("fighter_id,name,image_url,record,country_code")
        ) {
            filter {
                ilike("name", "%$query%")
            }
            limit(15)
        }.decodeList<FighterDto>()
    }
}