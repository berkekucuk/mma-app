package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FighterInteractionDto

interface FighterInteractionRemoteDataSource {
    suspend fun fetchInteractions(userId: String): List<FighterInteractionDto>
    suspend fun submitInteraction(userId: String, fighterId: String, interactionType: String): FighterInteractionDto
    suspend fun removeInteraction(interactionId: String)
}
