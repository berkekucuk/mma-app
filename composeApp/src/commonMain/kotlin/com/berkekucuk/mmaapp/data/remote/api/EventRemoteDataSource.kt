package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.EventDTO
import kotlin.time.Instant

interface EventRemoteDataSource {

    suspend fun fetchEventsByYear(year: Int): List<EventDTO>
    suspend fun fetchEventsAfter(date: Instant): List<EventDTO>
}

