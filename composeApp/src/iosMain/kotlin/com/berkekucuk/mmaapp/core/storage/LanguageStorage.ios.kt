package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSUserDefaults
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

class IosLanguageStorage : LanguageStorage {
    override fun save(code: String) {
        NSUserDefaults.standardUserDefaults.setObject(code, forKey = "language")
    }

    override fun load(): String {
        val stored = NSUserDefaults.standardUserDefaults.stringForKey("language")
        if (stored != null) return stored

        val preferredLanguage = NSLocale.preferredLanguages.firstOrNull() as? String
        return if (preferredLanguage?.startsWith("tr", ignoreCase = true) == true) "TR" else "EN"
    }
}
