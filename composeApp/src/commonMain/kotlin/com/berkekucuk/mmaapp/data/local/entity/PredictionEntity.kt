package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "predictions", primaryKeys = ["fight_id", "user_id"])
data class PredictionEntity(
    @ColumnInfo(name = "fight_id") val fightId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "predicted_winner_id") val predictedWinnerId: String,
    @ColumnInfo(name = "locked_odds") val lockedOdds: Int,
)
