package com.berkekucuk.mmaapp.domain.model

data class Fight(
    val fightId: String,
    val eventId: String,
    val methodType: String,
    val methodDetail: String,
    val roundSummary: String,
    val boutType: String,
    val weightClassLbs: Double?,
    val roundsFormat: String,
    val fightOrder: Int,
)



