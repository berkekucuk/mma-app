package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    suspend fun syncEvents(): Result<Unit>
    suspend fun getEventsByYear(year: Int): Result<Unit>
    suspend fun forceRefreshFeaturedTab(): Result<Unit>
    suspend fun forceRefreshUpcomingTab(): Result<Unit>
    suspend fun forceRefreshCompletedTab(year: Int): Result<Unit>
}
