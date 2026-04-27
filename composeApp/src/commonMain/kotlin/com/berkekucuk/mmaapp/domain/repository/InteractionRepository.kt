package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Interaction
import kotlinx.coroutines.flow.Flow

interface InteractionRepository {
    fun getInteractions(userId: String, type: String? = null): Flow<List<Interaction>>
    suspend fun syncInteractions(userId: String): Result<Unit>
    suspend fun addInteraction(userId: String, fighterId: String, interactionType: String): Result<Unit>
    suspend fun removeInteraction(interactionId: String): Result<Unit>
}
