package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getUpcomingEvents(): Flow<List<Event>>
    fun getCompletedEventsByYear(year: Int): Flow<List<Event>>
    fun getEventById(eventId: String): Flow<Event>
    suspend fun initializeEvents(): Result<Unit>
    suspend fun refreshPendingEvents(): Result<Unit>
    suspend fun refreshEventById(eventId: String): Result<Unit>
    suspend fun syncEventsByYear(year: Int, forceRefresh: Boolean = false): Result<Unit>
}
