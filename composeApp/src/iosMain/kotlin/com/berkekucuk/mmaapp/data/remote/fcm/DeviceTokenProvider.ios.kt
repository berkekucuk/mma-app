package com.berkekucuk.mmaapp.data.remote.fcm

actual class DeviceTokenProvider actual constructor() {
    actual val platform: String = "ios"
    actual suspend fun getToken(): String? = null
}
