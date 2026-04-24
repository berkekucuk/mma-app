package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDto
import kotlin.time.Instant

@Entity(tableName = "fights")
data class FightEntity(
    @PrimaryKey @ColumnInfo(name = "fight_id") val fightId: String,
    @ColumnInfo(name = "event_id") val eventId: String,
    @ColumnInfo(name = "event_name") val eventName: String?,
    @ColumnInfo(name = "event_date") val eventDate: Instant?,
    val methodType: String,
    val methodDetail: String,
    val roundSummary: String,
    val boutType: String,
    val weightClassLbs: Int?,
    val weightClassId: String,
    val roundsFormat: String,
    val fightOrder: Int,
    val participants: List<ParticipantDto>
)
