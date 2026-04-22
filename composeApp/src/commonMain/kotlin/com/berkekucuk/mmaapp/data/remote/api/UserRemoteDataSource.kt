package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.FightNotificationDto
import com.berkekucuk.mmaapp.data.remote.dto.UserDto

interface UserRemoteDataSource {

    suspend fun fetchUser(userId: String): UserDto
    suspend fun fetchUsers(limit: Int): List<UserDto>

    suspend fun updateUser(userId: String, fullName: String, username: String)

    suspend fun fetchFightNotifications(userId: String): List<FightNotificationDto>

    suspend fun upsertFightNotification(fightId: String, userId: String)

    suspend fun deleteFightNotification(fightId: String, userId: String)

    suspend fun addFavoriteFighter(userId: String, fighterId: String)

    suspend fun removeFavoriteFighter(userId: String, fighterId: String)
}
