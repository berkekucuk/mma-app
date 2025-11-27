package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class FightAPI(
    private val supabase: SupabaseClient
): FightRemoteDataSource {

    override suspend fun getFightsByEventId(eventId: String): List<FightDto> {

        return supabase.from("fights_view").select(
            columns = Columns.raw("""
                *,
                participants (
                    *,
                    fighters (*)
                )
            """.trimIndent())
        ) {
            filter {
                eq("event_id", eventId)
            }
            order("fight_order", order = Order.DESCENDING)
        }.decodeList<FightDto>()
    }
}
