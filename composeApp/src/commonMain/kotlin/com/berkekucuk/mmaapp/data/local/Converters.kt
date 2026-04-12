package com.berkekucuk.mmaapp.data.local

import androidx.room.TypeConverter
import com.berkekucuk.mmaapp.data.remote.dto.FightDto
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.data.remote.dto.MeasurementDto
import com.berkekucuk.mmaapp.data.remote.dto.RankedFighterDto
import com.berkekucuk.mmaapp.data.remote.dto.RecordDto
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

    private inline fun <reified T> decode(value: String?): T? {
        if (value.isNullOrEmpty()) return null
        return try {
            json.decodeFromString<T>(value)
        } catch (e: Exception) {
            null
        }
    }

    private inline fun <reified T> encodeList(value: List<T>?): String {
        return json.encodeToString(value ?: emptyList())
    }

    private inline fun <reified T> decodeList(value: String?): List<T> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromFightsList(value: List<FightDto>?): String = encodeList(value)

    @TypeConverter
    fun toFightsList(value: String?): List<FightDto> = decodeList(value)

    @TypeConverter
    fun fromFighterList(value: List<FighterDto>?): String = encodeList(value)

    @TypeConverter
    fun toFighterList(value: String?): List<FighterDto> = decodeList(value)

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toTimestamp(value: Instant?): Long? = value?.toEpochMilliseconds()

    @TypeConverter
    fun fromRecord(value: RecordDto?): String? = encode(value)

    @TypeConverter
    fun toRecord(value: String?): RecordDto? = decode(value)

    @TypeConverter
    fun fromMeasurement(value: MeasurementDto?): String? = encode(value)

    @TypeConverter
    fun toMeasurement(value: String?): MeasurementDto? = decode(value)

    @TypeConverter
    fun fromRankingsList(value: List<RankedFighterDto>?): String = encodeList(value)

    @TypeConverter
    fun toRankingsList(value: String?): List<RankedFighterDto> = decodeList(value)
}
