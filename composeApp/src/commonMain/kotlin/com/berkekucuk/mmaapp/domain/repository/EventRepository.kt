package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    suspend fun syncEvents(): Result<Unit>
    suspend fun refreshFeaturedEventTab(): Result<Unit>
    suspend fun refreshUpcomingEventsTab(): Result<Unit>
    suspend fun getEventsByYear(year: Int, forceRefresh: Boolean = false): Result<Unit>
}
