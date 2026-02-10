package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import kotlin.time.Instant

interface EventRemoteDataSource {
    suspend fun fetchEventsById(id: String): List<EventDto>
    suspend fun fetchEventsByYear(year: Int): List<EventDto>
    suspend fun fetchEventsAfter(date: Instant): List<EventDto>
}

