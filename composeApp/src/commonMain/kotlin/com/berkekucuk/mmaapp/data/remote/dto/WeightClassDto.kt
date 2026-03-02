package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeightClassDto(
    @SerialName("name") val name: String? = null,
    @SerialName("sort_order") val sortOrder: Int? = null
)