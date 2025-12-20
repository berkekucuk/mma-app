package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class FightAPI(
    private val client: SupabaseClient
): FightRemoteDataSource {

    private val columnsToSelect = """
            fight_id,
            event_id,
            method_type,
            method_detail,
            round_summary,
            bout_type,
            weight_class_lbs,
            weight_class_id,
            rounds_format,
            fight_order,
            participants(
                odds_value,
                odds_label,
                result,
                record_after_fight,
                is_red_corner,
                fighters(
                        fighter_id,
                        name,
                        image_url,
                        record,
                        height,
                        reach,
                        date_of_birth,
                        country_code
                )
            )
        """.trimIndent().replace("\n", "").replace(" ", "")

    override suspend fun fetchFightsByEvent(eventId: String): List<FightDto> {
        return client.from("fights").select(
            columns = Columns.raw(columnsToSelect)
        ) {
            filter {
                eq("event_id", eventId)
            }
        }.decodeList<FightDto>()
    }
}
