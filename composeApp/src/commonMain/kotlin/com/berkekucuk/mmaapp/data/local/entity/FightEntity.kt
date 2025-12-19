package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.berkekucuk.mmaapp.data.remote.dto.ParticipantDTO

@Entity(
    tableName = "fights",
    indices = [Index("event_id")]
)
data class FightEntity(
    @PrimaryKey  @ColumnInfo(name = "fight_id")  val fightId: String,
    @ColumnInfo(name = "event_id") val eventId: String,
    @ColumnInfo(name = "method_type") val methodType: String? = null,
    @ColumnInfo(name = "method_detail") val methodDetail: String? = null,
    @ColumnInfo(name = "round_summary") val roundSummary: String? = null,
    @ColumnInfo(name = "bout_type") val boutType: String? = null,
    @ColumnInfo(name = "weight_class_lbs") val weightClassLbs: Int? = null,
    @ColumnInfo(name = "weight_class_id") val weightClassId: String? = null,
    @ColumnInfo(name = "rounds_format") val roundsFormat: String? = null,
    @ColumnInfo(name = "fight_order") val fightOrder: Int? = null,
    @ColumnInfo(name = "participants_json") val participants: List<ParticipantDTO> = emptyList(),
)
