package com.berkekucuk.mmaapp.data.remote.api

interface DeviceTokenRemoteDataSource {
    suspend fun upsertToken(token: String, userId: String, platform: String)
    suspend fun deleteToken(token: String)
}
