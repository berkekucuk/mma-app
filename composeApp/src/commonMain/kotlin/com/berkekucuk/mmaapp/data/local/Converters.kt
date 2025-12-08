package com.berkekucuk.mmaapp.data.local

import androidx.room.TypeConverter
import com.berkekucuk.mmaapp.data.remote.dto.FightDTO
import kotlinx.serialization.json.Json
import kotlin.time.Instant

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromFightsList(fights: List<FightDTO>?): String {
        return json.encodeToString(fights ?: emptyList())
    }

    @TypeConverter
    fun toFightsList(fightsString: String?): List<FightDTO> {
        return if (fightsString.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                json.decodeFromString(fightsString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Instant?): Long? {
        return date?.toEpochMilliseconds()
    }
}