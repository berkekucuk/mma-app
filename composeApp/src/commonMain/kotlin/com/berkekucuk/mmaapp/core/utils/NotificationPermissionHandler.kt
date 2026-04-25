package com.berkekucuk.mmaapp.core.utils

import androidx.compose.runtime.Composable

@Composable
expect fun NotificationPermissionHandler(
    trigger: Boolean,
    onResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
)
