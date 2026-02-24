package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.RankingDto

interface RankingsRemoteDataSource {
    suspend fun fetchRankings(): List<RankingDto>
}