package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import androidx.core.content.edit

class AndroidLanguageStorage(context: Context) : LanguageStorage {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun save(code: String) {
        prefs.edit { putString("language", code) }
    }

    override fun load(): String {
        return prefs.getString("language", "EN") ?: "EN"
    }
}
