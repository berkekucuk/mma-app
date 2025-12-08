package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class EventDTO(
    @SerialName("event_id")
    val eventId: String,

    @SerialName("name")
    val name: String? = null,

    @SerialName("status")
    val status: String? = null,

    @SerialName("datetime_utc")
    val datetimeUtc: Instant? = null,

    @SerialName("venue")
    val venue: String? = null,

    @SerialName("location")
    val location: String? = null,

    @SerialName("fights")
    val fights: List<FightDTO> = emptyList()
)

@Serializable
data class FightDTO(
    @SerialName("method_type")
    val methodType: String? = null,

    @SerialName("method_detail")
    val methodDetail: String? = null,

    @SerialName("round_summary")
    val roundSummary: String? = null,

    @SerialName("weight_class_id")
    val weightClassId: String? = null,

    @SerialName("participants")
    val participants: List<ParticipantDTO> = emptyList()
)

@Serializable
data class ParticipantDTO(
    @SerialName("result")
    val result: String? = null,

    @SerialName("is_red_corner")
    val isRedCorner: Boolean? = null,

    @SerialName("record_after_fight")
    val recordAfterFight: RecordDTO? = null,

    @SerialName("fighters")
    val fighter: FighterDTO? = null
)

@Serializable
data class FighterDTO(
    @SerialName("name")
    val name: String? = null,

    @SerialName("record")
    val record: RecordDTO? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("country_code")
    val countryCode: String? = null
)

@Serializable
data class RecordDTO(
    @SerialName("wins")
    val wins: Int = 0,

    @SerialName("losses")
    val losses: Int = 0,

    @SerialName("draws")
    val draws: Int = 0
)