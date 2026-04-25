package com.berkekucuk.mmaapp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.setStatusBarStyle

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