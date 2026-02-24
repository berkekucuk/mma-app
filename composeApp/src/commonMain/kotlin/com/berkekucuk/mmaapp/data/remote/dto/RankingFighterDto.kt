package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingFighterDto(
    @SerialName("fighter_id") val fighterId: String,
    val name: String? = null,
    val nickname: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val record: RecordDto? = null,
    @SerialName("country_code") val countryCode: String? = null
)