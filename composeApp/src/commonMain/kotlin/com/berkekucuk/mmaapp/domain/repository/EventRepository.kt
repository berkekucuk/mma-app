package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    fun getEventById(eventId: String): Flow<Event>
    suspend fun syncEvents(): Result<Unit>
    suspend fun refreshEvents(): Result<Unit>
    suspend fun getEventsByYear(year: Int, forceRefresh: Boolean = false): Result<Unit>
}
