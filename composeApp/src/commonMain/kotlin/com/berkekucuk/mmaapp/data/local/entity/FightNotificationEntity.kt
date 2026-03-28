package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "fight_notifications", primaryKeys = ["fight_id", "user_id"])
data class FightNotificationEntity(
    @ColumnInfo(name = "fight_id") val fightId: String,
    @ColumnInfo(name = "user_id") val userId: String,
)
