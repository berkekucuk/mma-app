package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "fighter_fights",
    primaryKeys = ["fighter_id", "fight_id"],
    indices = [Index(value = ["fight_id"])]
)
data class FighterFightCrossRef(
    @ColumnInfo(name = "fighter_id") val fighterId: String,
    @ColumnInfo(name = "fight_id") val fightId: String
)
