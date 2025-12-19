package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDTO(
    val metric: Int? = null,
    val imperial: String? = null
)