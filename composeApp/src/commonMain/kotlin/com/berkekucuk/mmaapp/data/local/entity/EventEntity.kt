package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.berkekucuk.mmaapp.data.remote.dto.FightDTO
import kotlin.time.Instant

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = "event_id")
    val eventId: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "status")
    val status: String?,

    @ColumnInfo(name = "datetime_utc")
    val datetimeUtc: Instant?,

    @ColumnInfo(name = "venue")
    val venue: String?,

    @ColumnInfo(name = "location")
    val location: String?,

    // Thanks to TypeConverter, this list will be embedded in a single cell as JSON text
    @ColumnInfo(name = "fights_json")
    val fights: List<FightDTO>
)
