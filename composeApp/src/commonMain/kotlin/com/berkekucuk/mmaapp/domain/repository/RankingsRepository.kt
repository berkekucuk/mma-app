package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.WeightClassRanking

interface RankingsRepository {
    suspend fun getRankings(): Result<List<WeightClassRanking>>
}