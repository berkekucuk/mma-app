package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Ranking
import kotlinx.coroutines.flow.Flow

interface RankingRepository {
    fun getRankings(): Flow<List<Ranking>>
    suspend fun syncRankings(): Result<Unit>
}
