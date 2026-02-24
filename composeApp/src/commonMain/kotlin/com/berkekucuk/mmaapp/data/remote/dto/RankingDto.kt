package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingDto(
    @SerialName("weight_class_id") val weightClassId: String,
    @SerialName("rank_number") val rankNumber: Int,
    @SerialName("fighter_id") val fighterId: String,
    @SerialName("updated_at") val updatedAt: String? = null,
    val fighters: RankingFighterDto? = null,
    val weight_classes: WeightClassDto? = null,
)