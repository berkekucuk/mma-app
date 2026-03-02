package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingDto(
    @SerialName("weight_class_id") val weightClassId: String,
    @SerialName("rank_number") val rankNumber: Int? = null,
    @SerialName("fighters") val fighter: FighterDto? = null,
    @SerialName("weight_classes") val weightClass: WeightClassDto?
)