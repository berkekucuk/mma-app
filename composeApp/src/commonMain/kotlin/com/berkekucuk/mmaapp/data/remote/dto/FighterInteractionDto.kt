package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FighterInteractionDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("fighter_id") val fighterId: String,
    @SerialName("interaction_type") val interactionType: String,
    @SerialName("rank_number") val rankNumber: Int? = null,
    val fighter: FighterDto? = null
)
