package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithFavoriteFighters(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val favoriteFighters: List<UserFighterFavoriteEntity>
)
