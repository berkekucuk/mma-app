package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSUserDefaults

class IosLanguageStorage : LanguageStorage {
    override fun save(code: String) {
        NSUserDefaults.standardUserDefaults.setObject(code, forKey = "language")
    }

    override fun load(): String {
        return NSUserDefaults.standardUserDefaults.stringForKey("language") ?: "EN"
    }
}
