package com.berkekucuk.mmaapp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun SetStatusBarAppearance(isDarkTheme: Boolean) {
    LaunchedEffect(isDarkTheme) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.let {
            it.overrideUserInterfaceStyle = if (isDarkTheme) {
                UIUserInterfaceStyle.UIUserInterfaceStyleDark
            } else {
                UIUserInterfaceStyle.UIUserInterfaceStyleLight
            }
            it.setNeedsStatusBarAppearanceUpdate()
        }
    }
}