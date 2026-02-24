package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.mapper.toWeightClassRankings
import com.berkekucuk.mmaapp.data.remote.api.RankingsRemoteDataSource
import com.berkekucuk.mmaapp.domain.model.WeightClassRanking
import com.berkekucuk.mmaapp.domain.repository.RankingsRepository

class RankingsRepositoryImpl(
    private val remoteDataSource: RankingsRemoteDataSource
) : RankingsRepository {
    override suspend fun getRankings(): Result<List<WeightClassRanking>> {
        return runCatching {
            remoteDataSource.fetchRankings().toWeightClassRankings()
        }
    }
}