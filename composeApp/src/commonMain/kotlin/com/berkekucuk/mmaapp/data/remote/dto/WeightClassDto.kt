package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeightClassDto(
    val id: String,
    val name: String? = null,
    @SerialName("sort_order") val sortOrder: Int? = null,
    @SerialName("weight_limit") val weightLimit: Int? = null
)