package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.EventDto
import kotlin.time.Instant

interface EventRemoteDataSource {
    suspend fun fetchEvents(afterDate: Instant? = null): List<EventDto>
}