package com.berkekucuk.mmaapp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound

@Composable
actual fun NotificationPermissionHandler(
    trigger: Boolean,
    onResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    LaunchedEffect(trigger) {
        if (trigger) {
            val center = UNUserNotificationCenter.currentNotificationCenter()
            center.requestAuthorizationWithOptions(
                UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
            ) { granted, error ->
                onResult(granted)
            }
        }
    }
}
