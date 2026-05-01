package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSUserDefaults
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

class IosThemeStorage : ThemeStorage {
    private val themeKey = "theme_mode"

    override fun save(mode: String) {
        NSUserDefaults.standardUserDefaults.setObject(mode, forKey = themeKey)
    }

    override fun load(): String {
        val stored = NSUserDefaults.standardUserDefaults.stringForKey(themeKey)
        if (stored != null) return stored

        val userInterfaceStyle = UIScreen.mainScreen.traitCollection.userInterfaceStyle

        return if (userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark) {
            "DARK"
        } else {
            "LIGHT"
        }
    }
}