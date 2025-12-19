package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FighterDTO(
    @SerialName("fighter_id") val fighterId: String,
    val name: String? = null,
    val nickname: String? = null,
    val record: RecordDTO? = null,
    @SerialName("date_of_birth") val dateOfBirth: String? = null,
    val height: MeasurementDTO? = null,
    val reach: MeasurementDTO? = null,
    @SerialName("weight_class_id") val weightClassId: String? = null,
    val born: String? = null,
    @SerialName("fighting_out_of") val fightingOutOf: String? = null,
    val style: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
)
