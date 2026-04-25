package com.berkekucuk.mmaapp.core.utils

interface AppNotificationManager {
    fun showNotification(title: String, body: String, data: Map<String, String> = emptyMap())
}
