package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlin.time.Instant

class EventAPI(
    private val client: SupabaseClient
) : EventRemoteDataSource {

    private val columnsToSelect = """
            event_id,
            name,
            status,
            datetime_utc,
            event_year,
            venue,
            location,
            fights(
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
            )
            """.trimIndent().replace("\n", "").replace(" ", "")

    override suspend fun fetchEventsById(id: String): List<EventDto> {
        return client.from("events").select(
            columns = Columns.raw(columnsToSelect)
        ) {
            filter {
                eq("event_id", id)
            }
        }.decodeList<EventDto>()
    }

    override suspend fun fetchEventsByYear(year: Int): List<EventDto> {
        return client.from("events").select(
            columns = Columns.raw(columnsToSelect)
        ) {
            filter {
                eq("event_year", year)
                neq("status", "Cancelled")
            }
            order(column = "datetime_utc", order = Order.DESCENDING)
            order(column = "fight_order", order = Order.DESCENDING, referencedTable = "fights")
        }.decodeList<EventDto>()
    }

    override suspend fun fetchEventsAfter(date: Instant): List<EventDto> {
        return client.from("events").select(
            columns = Columns.raw(columnsToSelect)
        ) {
            filter {
                gte("datetime_utc", date)
                neq("status", "Cancelled")
            }
            order(column = "datetime_utc", order = Order.ASCENDING)
            order(column = "fight_order", order = Order.DESCENDING, referencedTable = "fights")
        }.decodeList<EventDto>()
    }
}
