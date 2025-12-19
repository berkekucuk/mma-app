package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDTO(
    @SerialName("odds_value") val oddsValue: Int? = null,
    @SerialName("odds_label") val oddsLabel: String? = null,
    val result: String? = null,
    @SerialName("record_after_fight") val recordAfterFight: RecordDTO? = null,
    @SerialName("is_red_corner") val isRedCorner: Boolean? = null,
    @SerialName("fighters") val fighter: FighterDTO? = null
)