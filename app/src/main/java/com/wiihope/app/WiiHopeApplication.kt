package com.wiihope.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.wiihope.app.core.audio.PlaybackChannels
import com.google.firebase.FirebaseApp

class WiiHopeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        createChannel(PlaybackChannels.MEDIA, getString(R.string.playback_channel_name), NotificationManager.IMPORTANCE_LOW)
        createChannel("wiihope_updates", "Novedades WiiHope", NotificationManager.IMPORTANCE_DEFAULT)
    }

    private fun createChannel(id: String, name: String, importance: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(id, name, importance).apply { setShowBadge(false) }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
