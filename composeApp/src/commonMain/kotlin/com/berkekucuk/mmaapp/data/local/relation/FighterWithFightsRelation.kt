package com.berkekucuk.mmaapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterFightCrossRef

data class FighterWithFightsRelation(
    @Embedded val fighter: FighterEntity,
    @Relation(
        parentColumn = "fighter_id",
        entityColumn = "fight_id",
        associateBy = Junction(FighterFightCrossRef::class)
    )
    val fights: List<FightEntity>
)