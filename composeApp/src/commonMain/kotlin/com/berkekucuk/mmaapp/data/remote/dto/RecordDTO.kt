package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecordDTO(
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0
)