package com.berkekucuk.mmaapp.data.remote.api

import com.berkekucuk.mmaapp.data.remote.dto.ProfileDto

interface ProfileRemoteDataSource {

    suspend fun fetchProfile(userId: String): ProfileDto

    suspend fun updateUsername(userId: String, username: String): ProfileDto
}
