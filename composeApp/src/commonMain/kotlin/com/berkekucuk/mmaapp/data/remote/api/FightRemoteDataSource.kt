package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightDto

interface FightRemoteDataSource {
    suspend fun fetchFightsByEvent(eventId: String): List<FightDto>
}