package com.berkekucuk.mmaapp.data.local

import androidx.room.TypeConverter
import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import kotlinx.serialization.json.Json
import kotlin.time.Instant

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    // Generic helper for encoding lists to JSON string
    private inline fun <reified T> encodeList(list: List<T>?): String {
        return json.encodeToString(list ?: emptyList())
    }

    // Generic helper for decoding JSON string to lists
    private inline fun <reified T> decodeList(jsonString: String?): List<T> {
        if (jsonString.isNullOrEmpty()) return emptyList()
        return try {
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromFightsList(fights: List<FightDto>?): String = encodeList(fights)

    @TypeConverter
    fun toFightsList(fightsString: String?): List<FightDto> = decodeList(fightsString)

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toTimestamp(date: Instant?): Long? = date?.toEpochMilliseconds()
}
