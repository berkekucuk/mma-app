package com.berkekucuk.mmaapp.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.berkekucuk.mmaapp.MainActivity
import com.berkekucuk.mmaapp.R
import android.media.RingtoneManager
import com.berkekucuk.mmaapp.core.presentation.AppLanguage
import com.berkekucuk.mmaapp.core.presentation.strings.AppStrings
import com.berkekucuk.mmaapp.core.presentation.strings.EnStrings
import com.berkekucuk.mmaapp.core.presentation.strings.TrStrings
import com.berkekucuk.mmaapp.core.storage.LanguageStorage
import kotlin.random.Random

class AndroidNotificationService(
    private val context: Context,
    private val languageStorage: LanguageStorage
) : NotificationService {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    private val strings: AppStrings
        get() = try {
            if (AppLanguage.valueOf(languageStorage.load()) == AppLanguage.TR) TrStrings else EnStrings
        } catch (_: Exception) {
            EnStrings
        }

    companion object {
        private const val CHANNEL_ID = "clinch_alerts_channel"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            strings.notificationChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = strings.notificationChannelDescription
            enableLights(true)
            enableVibration(true)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun showNotification(title: String, body: String, data: Map<String, String>) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}
