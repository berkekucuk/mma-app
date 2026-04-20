package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.berkekucuk.mmaapp.data.remote.dto.RecordDto

@Entity(
    tableName = "user_fighter_favorites",
    primaryKeys = ["user_id", "fighter_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFighterFavoriteEntity(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "fighter_id") val fighterId: String,
    val name: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "record_json") val record: RecordDto? = null
)
