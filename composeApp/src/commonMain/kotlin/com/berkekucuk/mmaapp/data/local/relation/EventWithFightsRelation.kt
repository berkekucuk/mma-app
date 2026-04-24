package com.berkekucuk.mmaapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.berkekucuk.mmaapp.data.local.entity.EventEntity
import com.berkekucuk.mmaapp.data.local.entity.FightEntity

data class EventWithFightsRelation(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "event_id",
        entityColumn = "event_id"
    )
    val fights: List<FightEntity>
)