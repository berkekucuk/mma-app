package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "fight_notifications",
    primaryKeys = ["fight_id", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FightNotificationEntity(
    @ColumnInfo(name = "fight_id") val fightId: String,
    @ColumnInfo(name = "user_id") val userId: String,
)
