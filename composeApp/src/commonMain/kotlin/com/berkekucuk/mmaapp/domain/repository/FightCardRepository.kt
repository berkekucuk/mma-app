package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.data.local.relation.FightCard
import kotlinx.coroutines.flow.Flow

interface FightCardRepository {

    fun getFightsByEvent(eventId: String): Flow<List<FightCard>>

    suspend fun syncFightsByEvent(eventId: String, status: String): Result<Unit>

}