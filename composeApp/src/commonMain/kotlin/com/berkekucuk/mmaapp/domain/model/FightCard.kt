package com.berkekucuk.mmaapp.domain.model

data class ParticipantWithFighterDomain(
    val participant: Participant,
    val fighter: Fighter
)

data class FightCardDomain(
    val fight: Fight,
    val participants: List<ParticipantWithFighterDomain>
)

