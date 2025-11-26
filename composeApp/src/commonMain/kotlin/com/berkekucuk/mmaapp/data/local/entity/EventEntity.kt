package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val eventId: String,
    val name: String,
    val status: String?,
    val datetimeUtc: Instant,
    val venue: String?,
    val location: String?
)