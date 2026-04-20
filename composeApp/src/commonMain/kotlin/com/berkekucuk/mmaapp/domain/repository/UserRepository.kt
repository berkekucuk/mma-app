package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<User?>
    suspend fun syncUser(userId: String): Result<Unit>
    suspend fun updateUser(userId: String, fullName: String, username: String): Result<Unit>
    fun getFightNotificationStatus(fightId: String, userId: String): Flow<Boolean>
    suspend fun addFightNotification(fightId: String, userId: String): Result<Unit>
    suspend fun removeFightNotification(fightId: String, userId: String): Result<Unit>
    suspend fun addFavoriteFighter(userId: String, fighter: Fighter): Result<Unit>
    suspend fun removeFavoriteFighter(userId: String, fighterId: String): Result<Unit>
}
