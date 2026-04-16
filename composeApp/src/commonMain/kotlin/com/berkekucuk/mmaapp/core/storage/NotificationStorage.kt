package com.berkekucuk.mmaapp.core.storage

interface NotificationStorage {
    fun save(isEnabled: Boolean)
    fun load(): Boolean
    fun openNotificationSettings()
}
