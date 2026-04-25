package com.berkekucuk.mmaapp.core.utils

interface NotificationService {
    fun showNotification(title: String, body: String, data: Map<String, String> = emptyMap())
}
