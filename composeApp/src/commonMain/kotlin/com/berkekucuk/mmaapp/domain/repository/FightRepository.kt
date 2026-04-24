package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Fight
import kotlinx.coroutines.flow.Flow

interface FightRepository {
    fun getFight(fightId: String): Flow<Fight>
    suspend fun syncFight(fightId: String): Result<Unit>
}
