package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class RankedFighter(
    val fighterId: String,
    val name: String,
    val imageUrl: String,
    val rank: Int,
    val isChampion: Boolean,
    val countryCode: String,
    val record: Record
)
