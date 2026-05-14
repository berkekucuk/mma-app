package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_users")
data class BlockedUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "blocked_user_id")
    val blockedUserId: String
)
