package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import androidx.core.content.edit

class AndroidMeasurementUnitStorage(context: Context) : MeasurementUnitStorage {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun save(unit: String) {
        prefs.edit { putString("measurement_unit", unit) }
    }

    override fun load(): String {
        return prefs.getString("measurement_unit", "METRIC") ?: "METRIC"
    }
}
