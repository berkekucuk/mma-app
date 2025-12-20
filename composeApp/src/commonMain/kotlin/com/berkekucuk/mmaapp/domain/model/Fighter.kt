package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable
import com.berkekucuk.mmaapp.domain.enums.WeightClass

@Immutable
data class Fighter(
    val fighterId: String,
    val name: String,
    val nickname: String,
    val record: Record,
    val dateOfBirth: String,
    val height: Measurement,
    val reach: Measurement,
    val weightClassId: WeightClass,
    val born: String,
    val fightingOutOf: String,
    val style: String,
    val countryCode: String,
    val imageUrl: String
)

