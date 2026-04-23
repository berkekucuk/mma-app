package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import kotlin.time.Instant

@Entity(tableName = "predictions", primaryKeys = ["fight_id", "user_id"])
data class PredictionEntity(
    @ColumnInfo(name = "prediction_id") val predictionId: String,
    @ColumnInfo(name = "fight_id") val fightId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "predicted_winner_id") val predictedWinnerId: String,
    @ColumnInfo(name = "points_earned") val pointsEarned: Int,
    @ColumnInfo(name = "is_correct") val isCorrect: Boolean?,
    @ColumnInfo(name = "locked_odds") val lockedOdds: Int?,
    @ColumnInfo(name = "created_at") val createdAt: Instant,
    @ColumnInfo(name = "fight_json") val fight: FightDto?
)
