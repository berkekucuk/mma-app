package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.RankingDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class RankingsSupabaseAPI(
    private val client: SupabaseClient
) : RankingsRemoteDataSource {

    override suspend fun fetchRankings(): List<RankingDto> {
        return client.from("rankings").select(
            columns = Columns.raw("""
                weight_class_id,
                rank_number,
                fighter_id,
                fighters(fighter_id, name, nickname, image_url, record, country_code),
                weight_classes(id, name, sort_order, weight_limit)
            """.trimIndent().replace("\n", ""))
        ) {
            order(column = "rank_number", order = Order.ASCENDING)
        }.decodeList<RankingDto>()
    }
}
