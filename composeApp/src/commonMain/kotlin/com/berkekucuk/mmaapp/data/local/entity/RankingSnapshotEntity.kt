package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "ranking_snapshots", primaryKeys = ["fighter_id", "weight_class_id"])
data class RankingSnapshotEntity(
    @ColumnInfo(name = "fighter_id") val fighterId: String,
    @ColumnInfo(name = "weight_class_id") val weightClassId: String,
    @ColumnInfo(name = "rank_number") val rankNumber: Int,
    @ColumnInfo(name = "snapshot_date") val snapshotDate: Long
)