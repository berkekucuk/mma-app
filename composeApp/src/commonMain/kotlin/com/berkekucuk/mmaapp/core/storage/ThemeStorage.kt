package com.berkekucuk.mmaapp.core.storage

interface ThemeStorage {
    fun save(mode: String)
    fun load(): String
}
