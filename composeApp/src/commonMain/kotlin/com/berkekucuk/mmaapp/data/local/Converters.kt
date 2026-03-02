package com.berkekucuk.mmaapp.data.local

import androidx.room.TypeConverter
import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.data.remote.dto.WeightClassDto
import kotlinx.serialization.json.Json
import kotlin.time.Instant

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private inline fun <reified T> encode(value: T?): String? {
        return value?.let { json.encodeToString(it) }
    }

    private inline fun <reified T> decode(jsonString: String?): T? {
        if (jsonString.isNullOrEmpty()) return null
        return try {
            json.decodeFromString<T>(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    private inline fun <reified T> encodeList(list: List<T>?): String {
        return json.encodeToString(list ?: emptyList())
    }

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
    fun fromFighter(value: FighterDto?): String? = encode(value)

    @TypeConverter
    fun toFighter(value: String?): FighterDto? = decode(value)

    @TypeConverter
    fun fromWeightClass(value: WeightClassDto?): String? = encode(value)

    @TypeConverter
    fun toWeightClass(value: String?): WeightClassDto? = decode(value)

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toTimestamp(date: Instant?): Long? = date?.toEpochMilliseconds()
}
