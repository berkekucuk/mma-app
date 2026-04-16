package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import androidx.core.content.edit

class AndroidThemeStorage(context: Context) : ThemeStorage {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun save(mode: String) {
        prefs.edit {
            putString("theme_mode", mode)
        }
    }
    override fun load(): String {
        return prefs.getString("theme_mode", "DARK") ?: "DARK"
    }
}