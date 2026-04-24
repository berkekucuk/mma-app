package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightDto

interface FightRemoteDataSource {
    suspend fun fetchFight(fightId: String): FightDto?
}
