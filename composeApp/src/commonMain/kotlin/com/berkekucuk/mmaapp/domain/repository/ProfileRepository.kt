package com.berkekucuk.mmaapp.domain.repository

import com.berkekucuk.mmaapp.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(userId: String): Flow<Profile?>
    suspend fun syncProfile(userId: String): Result<Unit>
    suspend fun updateUsername(userId: String, username: String): Result<Unit>
}
