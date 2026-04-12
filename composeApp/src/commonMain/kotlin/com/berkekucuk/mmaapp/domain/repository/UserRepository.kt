package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<User?>
    suspend fun syncUser(userId: String): Result<Unit>
    suspend fun updateUser(userId: String, fullName: String, username: String): Result<Unit>
    fun observeFightNotificationStatus(fightId: String, userId: String): Flow<Boolean>
    suspend fun syncFightNotificationStatus(fightId: String, userId: String): Result<Unit>
    suspend fun addFightNotification(fightId: String, userId: String): Result<Unit>
    suspend fun removeFightNotification(fightId: String, userId: String): Result<Unit>
}
