package com.berkekucuk.mmaapp.data.remote.fcm

import platform.Foundation.NSUserDefaults.Companion.standardUserDefaults

class IosDeviceTokenProvider : DeviceTokenProvider {
    override val platform: String = "ios"
    override suspend fun getToken(): String? {
        val token = standardUserDefaults.stringForKey("device_token")
        return token
    }
}
