package com.berkekucuk.mmaapp.domain.model

import kotlinx.serialization.Serializable

data class Fighter(
    val fighterId: String,
    val name: String,
    val nickname: String,
    val weightClassId: String,
    val record: FighterRecord?,
    val height: Measurement?,
    val reach: Measurement?,
    val style: String,
    val dateOfBirth: String,
    val born: String,
    val fightingOutOf: String,
    val countryCode: String,
    val imageUrl: String,
    )

@Serializable
data class FighterRecord(
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0
)

@Serializable
data class Measurement(
    val metric: Double? = null,
    val imperial: String? = null
)