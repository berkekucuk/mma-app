package com.berkekucuk.mmaapp.domain.model
data class Participant(
    val id: Int,
    val fightId: String,
    val fighterId: String,
    val oddsValue: Int?,
    val oddsLabel: String,
    val result: Result,
    val recordAfterFight: FighterRecord?,
    val isRedCorner: Boolean,
    )

enum class Result{
    WIN,
    LOSS,
    DRAW,
    NO_CONTEST,
    PENDING,
    CANCELLED,
    FIZZLED,
    UNKNOWN
}