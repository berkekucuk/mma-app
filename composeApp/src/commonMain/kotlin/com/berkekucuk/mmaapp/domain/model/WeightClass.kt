package com.berkekucuk.mmaapp.domain.model

data class WeightClass(
    val id: String,
    val sortOrder: Int,
    val rankings: List<RankedFighter>
) {
    val isWomens: Boolean get() = id in setOf("womens_p4p", "SW", "W_FLW", "W_BW")
}
