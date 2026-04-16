package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSUserDefaults

class IosThemeStorage : ThemeStorage {
    override fun save(mode: String) {
        NSUserDefaults.standardUserDefaults.setObject(mode, forKey = "theme_mode")
    }

    override fun load(): String {
        return NSUserDefaults.standardUserDefaults.stringForKey("theme_mode") ?: "DARK"
    }
}
