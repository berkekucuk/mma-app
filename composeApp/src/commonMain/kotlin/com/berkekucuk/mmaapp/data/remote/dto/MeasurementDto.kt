package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    val metric: Int? = null,
    val imperial: String? = null
)