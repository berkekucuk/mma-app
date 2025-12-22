package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Fight
import kotlinx.coroutines.flow.Flow

interface FightRepository {
    fun getFightsByEvent(eventId: String): Flow<List<Fight>>
    suspend fun syncFights(eventId: String, eventStatus: String, forceRefresh: Boolean): Result<Unit>
}
