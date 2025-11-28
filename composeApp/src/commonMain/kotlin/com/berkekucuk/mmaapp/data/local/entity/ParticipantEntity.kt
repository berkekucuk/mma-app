package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "participants",
    foreignKeys = [
        ForeignKey(
            entity = FightEntity::class,
            parentColumns = ["fightId"],
            childColumns = ["fightId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FighterEntity::class,
            parentColumns = ["fighterId"],
            childColumns = ["fighterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("fightId"), Index("fighterId")]
)
data class ParticipantEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val fightId: String,
    val fighterId: String,
    val oddsValue: Int?,
    val oddsLabel: String?,
    val result: String?,
    val recordAfterFight: String?,
    val isWinner: Boolean
)