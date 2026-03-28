package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(userId: String): Flow<Profile?>
    suspend fun syncProfile(userId: String): Result<Unit>
    suspend fun updateProfile(userId: String, fullName: String, username: String): Result<Unit>
    fun observeFightNotificationStatus(fightId: String, userId: String): Flow<Boolean>
    suspend fun syncFightNotificationStatus(fightId: String, userId: String): Result<Unit>
    suspend fun addFightNotification(fightId: String, userId: String): Result<Unit>
    suspend fun removeFightNotification(fightId: String, userId: String): Result<Unit>
}
