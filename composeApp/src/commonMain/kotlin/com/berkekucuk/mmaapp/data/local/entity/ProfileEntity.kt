package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val username: String? = null,
    @ColumnInfo(name = "full_name") val fullName: String? = null,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String? = null,
    @ColumnInfo(name = "favorite_fighter_id") val favoriteFighterId: String? = null,
)
