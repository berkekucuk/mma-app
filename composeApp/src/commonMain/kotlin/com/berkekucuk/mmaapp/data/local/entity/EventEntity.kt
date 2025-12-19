package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.berkekucuk.mmaapp.data.remote.dto.FightDTO
import kotlin.time.Instant

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey @ColumnInfo(name = "event_id") val eventId: String,
    val status: String? = null,
    val name: String? = null,
    @ColumnInfo(name = "datetime_utc") val datetimeUtc: Instant? = null,
    val venue: String? = null,
    val location: String? = null,
    // Thanks to TypeConverter, this list will be embedded in a single cell as JSON text
    @ColumnInfo(name = "fights_json") val fights: List<FightDTO>
)
