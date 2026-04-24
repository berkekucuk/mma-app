package com.berkekucuk.mmaapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.local.entity.PredictionEntity

data class PredictionWithFightRelation(
    @Embedded val prediction: PredictionEntity,
    @Relation(
        parentColumn = "fight_id",
        entityColumn = "fight_id"
    )
    val fight: FightEntity?
)