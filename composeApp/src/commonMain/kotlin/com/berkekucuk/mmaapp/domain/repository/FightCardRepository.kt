package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.FightCardDomain
import kotlinx.coroutines.flow.Flow

interface FightCardRepository {

    fun getFightsByEvent(eventId: String): Flow<List<FightCardDomain>>

    suspend fun syncFightsByEvent(eventId: String, status: String): Result<Unit>

}