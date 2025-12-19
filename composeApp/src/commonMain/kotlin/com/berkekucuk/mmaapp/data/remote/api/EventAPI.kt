package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.EventDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

class EventAPI(
    private val client: SupabaseClient
) : EventRemoteDataSource {

    private val columnsToSelect = """
            event_id,
            name,
            status,
            datetime_utc,
            venue,
            location,
            fights(
                fight_id,
                event_id,
                method_type,
                method_detail,
                round_summary,
                weight_class_id,
                participants(
                    result,
                    record_after_fight,
                    is_red_corner,
                    fighters(
                        fighter_id,
                        name,
                        record,
                        image_url,
                        country_code
                    )
                )
            )
            """.trimIndent().replace("\n", "").replace(" ", "")


    override suspend fun fetchEventsByYear(year: Int): List<EventDTO> {
        val startOfYear = LocalDateTime(year, 1, 1, 0, 0, 0).toInstant(TimeZone.UTC)
        val endOfYear = LocalDateTime(year, 12, 31, 23, 59, 59).toInstant(TimeZone.UTC)
        return fetchEventsInternal(start = startOfYear, end = endOfYear)
    }

    override suspend fun fetchEventsAfter(date: Instant): List<EventDTO> {
        return fetchEventsInternal(start = date, end = null)
    }

    private suspend fun fetchEventsInternal(start: Instant, end: Instant?): List<EventDTO> {
        return client.from("events").select(
            columns = Columns.raw(columnsToSelect)
        ) {
            filter {
                gte("datetime_utc", start)

                if (end != null) {
                    lte("datetime_utc", end)
                }

                neq("status", "Cancelled")
            }

            order(column = "datetime_utc", order = Order.ASCENDING)
            order(column = "fight_order", order = Order.DESCENDING, nullsFirst = false, referencedTable = "fights")
            limit(count = 1, referencedTable = "fights")

        }.decodeList<EventDTO>()
    }
}
