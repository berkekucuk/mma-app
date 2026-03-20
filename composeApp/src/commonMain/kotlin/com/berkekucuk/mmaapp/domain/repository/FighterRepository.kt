package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Fighter
import kotlinx.coroutines.flow.Flow

interface FighterRepository {
    fun getFighterById(fighterId: String): Flow<Fighter>
    suspend fun syncFighter(fighterId: String): Result<Unit>
    suspend fun searchFighters(query: String): Result<List<Fighter>>
}
