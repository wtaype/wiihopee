package com.wiihope.app.core.notifications

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.wiihope.app.MainActivity
import com.wiihope.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WiiHopeMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: "WiiHope"
        val body = message.notification?.body ?: message.data["body"] ?: "Tienes una nueva novedad"
        val screen = message.data["screen"] ?: "home"
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("screen", screen)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            screen.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(this, "wiihope_updates")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        getSystemService(android.app.NotificationManager::class.java).notify(System.currentTimeMillis().toInt(), notification)
    }
}
