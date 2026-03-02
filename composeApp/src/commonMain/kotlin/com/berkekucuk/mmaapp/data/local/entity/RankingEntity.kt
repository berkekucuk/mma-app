package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.berkekucuk.mmaapp.data.remote.dto.FighterDto
import com.berkekucuk.mmaapp.data.remote.dto.WeightClassDto

@Entity(tableName = "rankings", primaryKeys = ["weight_class_id", "rank_number"])
data class RankingEntity(
    @ColumnInfo(name = "weight_class_id") val weightClassId: String,
    @ColumnInfo(name = "rank_number") val rankNumber: Int,
    @ColumnInfo(name = "fighter_json") val fighter: FighterDto? = null,
    @ColumnInfo(name = "weight_class_json") val weightClass: WeightClassDto?
)
