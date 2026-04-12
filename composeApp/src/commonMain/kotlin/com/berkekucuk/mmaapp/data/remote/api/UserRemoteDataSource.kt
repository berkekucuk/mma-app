package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.UserDto

interface UserRemoteDataSource {

    suspend fun fetchUser(userId: String): UserDto

    suspend fun updateUser(userId: String, fullName: String, username: String)

    suspend fun isFightNotificationEnabled(fightId: String, userId: String): Boolean

    suspend fun upsertFightNotification(fightId: String, userId: String)

    suspend fun deleteFightNotification(fightId: String, userId: String)
}
