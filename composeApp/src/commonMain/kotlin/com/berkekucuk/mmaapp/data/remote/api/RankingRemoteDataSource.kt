package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.RankingDto

interface RankingRemoteDataSource {
    suspend fun fetchRankings(): List<RankingDto>
}