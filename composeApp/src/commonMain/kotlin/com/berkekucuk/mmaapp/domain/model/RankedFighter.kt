package com.berkekucuk.mmaapp.domain.model

data class RankedFighter(
    val rankNumber: Int,
    val fighter: Fighter?,
) {
    val isChampion: Boolean get() = rankNumber == 0
}