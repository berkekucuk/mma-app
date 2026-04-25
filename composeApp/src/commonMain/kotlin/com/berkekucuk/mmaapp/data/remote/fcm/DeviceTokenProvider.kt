package com.berkekucuk.mmaapp.data.remote.fcm

interface DeviceTokenProvider {
    val platform: String
    suspend fun getToken(): String?
}
