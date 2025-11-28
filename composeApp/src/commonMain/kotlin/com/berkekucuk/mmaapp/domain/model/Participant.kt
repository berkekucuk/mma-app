package com.berkekucuk.mmaapp.domain.model
data class Participant(
    val id: Int,
    val fightId: String,
    val fighterId: String,
    val oddsValue: Int?,
    val oddsLabel: String,
    val result: String,
    val recordAfterFight: FighterRecord?,
    val isWinner: Boolean,
    )

