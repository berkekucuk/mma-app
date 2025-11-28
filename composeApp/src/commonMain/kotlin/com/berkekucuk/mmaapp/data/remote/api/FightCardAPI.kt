package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightCardDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class FightCardAPI(
    private val supabase: SupabaseClient
): FightCardRemoteDataSource {

    override suspend fun getFightCardsByEvent(eventId: String): List<FightCardDto> {

        val columnsToSelect = """
        fight_id, event_id, method_type, method_detail, round_summary, bout_type, weight_class_lbs, rounds_format, fight_order,
        participants (
            id, fight_id, fighter_id, odds_value, odds_label, result, record_after_fight,
            fighters (
                fighter_id, name, nickname, image_url, record, height, reach, weight_class_id, date_of_birth, born, fighting_out_of, style, country_code
            )
        )
    """.trimIndent()

        return supabase.from("fights").select(
            columns = Columns.raw(columnsToSelect)
        ) {
            filter {
                eq("event_id", eventId)
            }
            order("fight_order", order = Order.DESCENDING)
        }.decodeList<FightCardDto>()
    }
}
