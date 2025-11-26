package com.berkekucuk.mmaapp.data.remote

import com.berkekucuk.mmaapp.data.dto.EventDto
import kotlin.time.Instant

interface EventRemoteDataSource {
    suspend fun fetchEvents(afterDate: Instant? = null): List<EventDto>
}