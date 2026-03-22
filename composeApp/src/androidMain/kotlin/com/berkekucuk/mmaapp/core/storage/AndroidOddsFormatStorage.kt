package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import androidx.core.content.edit

class AndroidOddsFormatStorage(context: Context) : OddsFormatStorage {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun save(format: String) {
        prefs.edit { putString("odds_format", format) }
    }

    override fun load(): String {
        return prefs.getString("odds_format", "DECIMAL") ?: "DECIMAL"
    }
}
