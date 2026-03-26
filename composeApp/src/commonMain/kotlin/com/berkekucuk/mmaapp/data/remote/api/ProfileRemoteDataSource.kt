package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.ProfileDto

interface ProfileRemoteDataSource {

    suspend fun fetchProfile(userId: String): ProfileDto

    suspend fun updateProfile(userId: String, fullName: String, username: String)

    suspend fun isFightNotificationEnabled(fightId: String, userId: String): Boolean

    suspend fun upsertFightNotification(fightId: String, userId: String)

    suspend fun deleteFightNotification(fightId: String, userId: String)
}
