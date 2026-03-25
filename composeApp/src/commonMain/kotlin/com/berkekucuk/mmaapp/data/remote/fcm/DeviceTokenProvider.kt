package com.berkekucuk.mmaapp.data.remote.fcm

expect class DeviceTokenProvider() {
    val platform: String
    suspend fun getToken(): String?
}
