package com.berkekucuk.mmaapp.core.storage

interface OddsFormatStorage {
    fun save(format: String)
    fun load(): String
}
