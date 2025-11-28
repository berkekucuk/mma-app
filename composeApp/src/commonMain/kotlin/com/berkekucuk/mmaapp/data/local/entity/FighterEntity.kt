package com.berkekucuk.mmaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fighters")
data class FighterEntity(
    @PrimaryKey
    val fighterId: String,
    val name: String?,
    val nickname: String?,
    val weightClassId: String?,
    val record: String?,
    val height: String?,
    val reach: String?,
    val style: String?,
    val dateOfBirth: String?,
    val born: String?,
    val fightingOutOf: String?,
    val countryCode: String?,
    val imageUrl: String?,
    )