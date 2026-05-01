package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit

class AndroidThemeStorage(private val context: Context) : ThemeStorage {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun save(mode: String) {
        prefs.edit {
            putString("theme_mode", mode)
        }
    }

    override fun load(): String {
        val stored = prefs.getString("theme_mode", null)
        if (stored != null) return stored

        val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        return when (uiMode) {
            Configuration.UI_MODE_NIGHT_YES -> "DARK"
            Configuration.UI_MODE_NIGHT_NO -> "LIGHT"
            else -> "DARK"
        }
    }
}