package com.berkekucuk.mmaapp.core.storage

import platform.Foundation.NSUserDefaults

class IosMeasurementUnitStorage : MeasurementUnitStorage {
    override fun save(unit: String) {
        NSUserDefaults.standardUserDefaults.setObject(unit, forKey = "measurement_unit")
    }

    override fun load(): String {
        return NSUserDefaults.standardUserDefaults.stringForKey("measurement_unit") ?: "METRIC"
    }
}
