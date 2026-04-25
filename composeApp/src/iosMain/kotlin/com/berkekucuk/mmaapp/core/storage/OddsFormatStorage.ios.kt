package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSUserDefaults

class IosOddsFormatStorage : OddsFormatStorage {
    override fun save(format: String) {
        NSUserDefaults.standardUserDefaults.setObject(format, forKey = "odds_format")
    }

    override fun load(): String {
        return NSUserDefaults.standardUserDefaults.stringForKey("odds_format") ?: "DECIMAL"
    }
}
