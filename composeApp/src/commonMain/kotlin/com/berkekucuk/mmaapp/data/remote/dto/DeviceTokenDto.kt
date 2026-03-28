package com.berkekucuk.mmaapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceTokenDto(
    @SerialName("fcm_token") val fcmToken: String,
    @SerialName("user_id") val userId: String,
    @SerialName("platform") val platform: String
)
