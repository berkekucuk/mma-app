package com.berkekucuk.mmaapp.domain.model

data class Ranking(
    val weightClassId: String,
    val rankNumber: Int,
    val fighter: Fighter?,
    val weightClass: WeightClass?,
    val rankChange: Int? = null
)