package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import com.berkekucuk.mmaapp.domain.enums.Result

@Immutable
data class Participant(
    val name: String,
    val record: String,
    val imageUrl: String?,
    val countryCode: String?,
    val isRedCorner: Boolean?,
    val result: Result,
    val recordAfterFight: String?
)