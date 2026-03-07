package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.RankingDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class RankingSupabaseAPI(
    private val client: SupabaseClient
) : RankingRemoteDataSource {

    private val columnsToSelect = """
            weight_class_id,
            rank_number,
            fighters(fighter_id, name, image_url, record, country_code),
            weight_classes(name, sort_order)
    """.trimIndent().replace("\n", "").replace(" ", "")

    override suspend fun fetchRankings(): List<RankingDto> {
        return client.from("rankings").select(
            columns = Columns.raw(columnsToSelect)
        ).decodeList<RankingDto>()
    }
}