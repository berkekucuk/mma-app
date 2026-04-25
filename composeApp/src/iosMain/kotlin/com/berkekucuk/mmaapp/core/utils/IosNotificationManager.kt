package com.berkekucuk.mmaapp.core.utils

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNNotificationSound

class IosNotificationManager : AppNotificationManager {

    override fun showNotification(title: String, body: String, data: Map<String, String>) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(body)
            setSound(UNNotificationSound.defaultSound())
            @Suppress("UNCHECKED_CAST")
            setUserInfo(data as Map<Any?, *>)
        }

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = platform.Foundation.NSUUID().UUIDString(),
            content = content,
            trigger = null
        )

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error showing notification: ${error.localizedDescription}")
            }
        }
    }
}
