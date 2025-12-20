package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordDto(
    val wins: Int? = null,
    val losses: Int? = null,
    val draws: Int? = null
)