package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "synced_years")
data class SyncedYearEntity(
    @PrimaryKey val year: Int
)
