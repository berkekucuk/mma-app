package com.berkekucuk.mmaapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity

data class ParticipantWithFighter(
    @Embedded
    val participant: ParticipantEntity,

    @Relation(
        parentColumn = "fighterId",
        entityColumn = "fighterId"
    )
    val fighter: FighterEntity?
)

data class PopulatedFight(
    @Embedded
    val fight: FightEntity,

    @Relation(
        entity = ParticipantEntity::class,
        parentColumn = "fightId",
        entityColumn = "fightId"
    )
    val participants: List<ParticipantWithFighter>
)