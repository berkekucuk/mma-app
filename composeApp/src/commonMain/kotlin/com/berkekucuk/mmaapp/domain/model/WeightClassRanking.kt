package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeightClassRanking(
    val weightClassId: String,
    val weightClassName: String,
    val sortOrder: Int,
    val champion: RankedFighter,
    val rankedFighters: List<RankedFighter>
)