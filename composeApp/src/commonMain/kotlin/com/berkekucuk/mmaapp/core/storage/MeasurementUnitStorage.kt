package com.berkekucuk.mmaapp.core.storage

interface MeasurementUnitStorage {
    fun save(unit: String)
    fun load(): String
}
