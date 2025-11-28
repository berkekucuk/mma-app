package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fights",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class FightEntity(
    @PrimaryKey
    val fightId: String,
    val eventId: String,
    val methodType: String?,
    val methodDetail: String?,
    val roundSummary: String?,
    val boutType: String?,
    val weightClassLbs: Double?,
    val roundsFormat: String?,
    val fightOrder: Int?,
)