package com.berkekucuk.mmaapp.core.storage

interface LanguageStorage {
    fun save(code: String)
    fun load(): String
}
