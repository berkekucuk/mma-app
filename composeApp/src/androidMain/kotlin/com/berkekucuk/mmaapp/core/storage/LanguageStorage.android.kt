package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import androidx.core.content.edit
import java.util.Locale

class AndroidLanguageStorage(context: Context) : LanguageStorage {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun save(code: String) {
        prefs.edit { putString("language", code) }
    }

    override fun load(): String {
        val stored = prefs.getString("language", null)
        if (stored != null) return stored

        val systemLanguage = Locale.getDefault().language
        return if (systemLanguage.lowercase() == "tr") "TR" else "EN"
    }
}
