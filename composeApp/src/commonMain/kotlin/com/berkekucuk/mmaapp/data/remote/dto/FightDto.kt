package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FightDto(
    @SerialName("fight_id") val fightId: String,
    @SerialName("event_id") val eventId: String,
    @SerialName("method_type") val methodType: String? = null,
    @SerialName("method_detail") val methodDetail: String? = null,
    @SerialName("round_summary") val roundSummary: String? = null,
    @SerialName("bout_type") val boutType: String? = null,
    @SerialName("weight_class_lbs") val weightClassLbs: Int? = null,
    @SerialName("weight_class_id") val weightClassId: String? = null,
    @SerialName("rounds_format") val roundsFormat: String? = null,
    @SerialName("fight_order") val fightOrder: Int? = null,
    @SerialName("participants") val participants: List<ParticipantDto>? = null
)