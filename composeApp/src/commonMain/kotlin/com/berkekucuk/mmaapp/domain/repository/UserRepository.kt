package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Fighter
import com.berkekucuk.mmaapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<User?>
    fun getUsers(limit: Int): Flow<List<User>>
    suspend fun syncUser(userId: String): Result<Unit>
    suspend fun syncUsers(limit: Int): Result<Unit>
    suspend fun updateUser(userId: String, fullName: String, username: String): Result<Unit>
    fun getFightNotificationStatus(fightId: String, userId: String): Flow<Boolean>
    suspend fun syncFightNotifications(userId: String): Result<Unit>
    suspend fun addFightNotification(fightId: String, userId: String): Result<Unit>
    suspend fun removeFightNotification(fightId: String, userId: String): Result<Unit>
    suspend fun addFavoriteFighter(userId: String, fighter: Fighter): Result<Unit>
    suspend fun removeFavoriteFighter(userId: String, fighterId: String): Result<Unit>
    suspend fun submitPrediction(userId: String, fightId: String, predictedWinnerId: String, lockedOdds: Int): Result<Unit>
}
