package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "blocked_users", primaryKeys = ["blocker_user_id", "blocked_user_id"])
data class BlockedUserEntity(
    @ColumnInfo(name = "blocker_user_id")
    val blockerUserId: String,
    @ColumnInfo(name = "blocked_user_id")
    val blockedUserId: String
)
