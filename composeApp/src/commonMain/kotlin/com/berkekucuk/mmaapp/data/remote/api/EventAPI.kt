package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlin.time.Instant

class EventAPI(
    private val supabase: SupabaseClient
) : EventRemoteDataSource {

    override suspend fun fetchEvents(afterDate: Instant?): List<EventDto> {
        return supabase.from("events_view")
            .select {
                if (afterDate != null) {
                    filter {
                        gte("datetime_utc", afterDate)
                    }
                }
                order("datetime_utc", order = Order.DESCENDING)
            }
            .decodeList<EventDto>()
    }
}