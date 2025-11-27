package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ParticipantDto(
    val id: Int,
    @SerialName("fight_id") val fightId: String,
    @SerialName("fighter_id") val fighterId: String,
    @SerialName("odds_value") val oddsValue: Int? = null,
    @SerialName("odds_label") val oddsLabel: String? = null,
    val result: String? = null,
    @SerialName("record_after_fight") val recordAfterFight: JsonElement? = null,
    @SerialName("fighters") val fighter: FighterDto? = null
)