package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit

class AndroidNotificationStorage(private val context: Context) : NotificationStorage {
    override fun save(isEnabled: Boolean) {
        // Controlled by system
    }

    override suspend fun load(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    override fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    private val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    override fun hasRequestedPermission(): Boolean {
        return prefs.getBoolean("has_requested_permission", false)
    }

    override fun setRequestedPermission(requested: Boolean) {
        prefs.edit { putBoolean("has_requested_permission", requested) }
    }
}
