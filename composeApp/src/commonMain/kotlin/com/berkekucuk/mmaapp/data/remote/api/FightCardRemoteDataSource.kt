package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightCardDto

interface FightCardRemoteDataSource {
    suspend fun getFightCardsByEvent(eventId: String): List<FightCardDto>
}