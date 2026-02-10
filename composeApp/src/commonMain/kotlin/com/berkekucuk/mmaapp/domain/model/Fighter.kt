package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import com.berkekucuk.mmaapp.domain.enums.WeightClass

@Immutable
data class Fighter(
    val fighterId: String,
    val name: String,
    val nickname: String,
    val imageUrl: String,
    val record: Record,
    val height: Measurement,
    val reach: Measurement,
    val weightClassId: WeightClass,
    val dateOfBirth: String,
    val born: String,
    val fightingOutOf: String,
    val countryCode: String,
)

