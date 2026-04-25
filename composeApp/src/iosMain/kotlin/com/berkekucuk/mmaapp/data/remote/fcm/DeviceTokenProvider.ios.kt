package com.berkekucuk.mmaapp.data.remote.fcm

class IosDeviceTokenProvider : DeviceTokenProvider {
    override val platform: String = "ios"
    override suspend fun getToken(): String? = null
}
