package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.relation.FightCard
import com.berkekucuk.mmaapp.data.local.relation.ParticipantWithFighter
import com.berkekucuk.mmaapp.domain.model.FightCardDomain
import com.berkekucuk.mmaapp.domain.model.ParticipantWithFighterDomain

fun ParticipantWithFighter.toDomain(): ParticipantWithFighterDomain {
    return ParticipantWithFighterDomain(
        participant = participant.toDomain(),
        fighter = fighter.toDomain()
    )
}

fun FightCard.toDomain(): FightCardDomain {
    return FightCardDomain(
        fight = fight.toDomain(),
        participants = participants.map { it.toDomain() }
    )
}


