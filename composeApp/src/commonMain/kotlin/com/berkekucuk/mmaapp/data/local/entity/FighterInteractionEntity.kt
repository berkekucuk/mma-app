package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_fighter_interactions")
data class FighterInteractionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "fighter_id") val fighterId: String,
    @ColumnInfo(name = "interaction_type") val interactionType: String,
    @ColumnInfo(name = "rank_number") val rankNumber: Int,
)
