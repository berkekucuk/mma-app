package com.berkekucuk.mmaapp.data.mapper

import com.berkekucuk.mmaapp.data.local.entity.InteractionEntity
import com.berkekucuk.mmaapp.data.local.relation.InteractionWithFighterRelation
import com.berkekucuk.mmaapp.data.remote.dto.InteractionDto
import com.berkekucuk.mmaapp.domain.model.Interaction

fun InteractionDto.toEntity(): InteractionEntity {
    return InteractionEntity(
        id = id,
        userId = userId,
        fighterId = fighterId,
        interactionType = interactionType,
        rankNumber = rankNumber ?: 0,
    )
}

fun InteractionWithFighterRelation.toDomain(): Interaction {
    return Interaction(
        id = interaction.id,
        userId = interaction.userId,
        fighterId = interaction.fighterId,
        interactionType = interaction.interactionType,
        rankNumber = interaction.rankNumber,
        fighter = fighter?.toDomain()
    )
}