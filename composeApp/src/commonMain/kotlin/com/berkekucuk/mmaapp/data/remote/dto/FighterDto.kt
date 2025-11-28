package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FighterDto(
    @SerialName("fighter_id") val fighterId: String,
    val name: String,
    val nickname: String? = null,
    @SerialName("weight_class_id") val weightClassId: String? = null,
    val record: JsonElement? = null,
    val height: JsonElement? = null,
    val reach: JsonElement? = null,
    val style: String? = null,
    @SerialName("date_of_birth") val dateOfBirth: String? = null,
    val born: String? = null,
    @SerialName("fighting_out_of") val fightingOutOf: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
    @SerialName("image_url") val imageUrl: String? = null
    )



