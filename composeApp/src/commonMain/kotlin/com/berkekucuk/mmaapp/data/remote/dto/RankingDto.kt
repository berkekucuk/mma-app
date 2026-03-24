package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingDto(
    @SerialName("weight_class_id") val weightClassId: String,
    @SerialName("rank_number") val rankNumber: Int,
    @SerialName("fighters") val fighter: FighterDto? = null,
)