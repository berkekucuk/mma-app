package com.berkekucuk.mmaapp.core.storage

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

class AndroidNotificationStorage(private val context: Context) : NotificationStorage {
    override fun save(isEnabled: Boolean) {
        // Controlled by system
    }

    override fun load(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    override fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
