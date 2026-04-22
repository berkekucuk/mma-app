package com.berkekucuk.mmaapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val id: String,
    val username: String?,
    val fullName: String?,
    val avatarUrl: String?,
    val totalPoints: Int,
    val favoriteFighters: List<RankedFighter>,
)
