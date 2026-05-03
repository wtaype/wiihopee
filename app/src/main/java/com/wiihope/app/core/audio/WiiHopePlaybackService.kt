package com.wiihope.app.core.audio

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.wiihope.app.MainActivity

class WiiHopePlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private val playerListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) = updateSessionActivity(player.currentMediaItem)
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) = updateSessionActivity(mediaItem)
    }

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true,
            )
            repeatMode = Player.REPEAT_MODE_OFF
            addListener(playerListener)
        }
        mediaSession = MediaSession.Builder(this, player)
            .setId("wiihope-session")
            .setSessionActivity(screenPendingIntent("music"))
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player
        if (player == null || !player.isPlaying) stopSelf()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.removeListener(playerListener)
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }

    private fun updateSessionActivity(mediaItem: MediaItem?) {
        val screen = when (mediaItem?.mediaMetadata?.extras?.getString("source")) {
            "Prayer" -> "prayer"
            "Bible" -> "bible"
            else -> "music"
        }
        mediaSession?.setSessionActivity(screenPendingIntent(screen))
    }

    private fun screenPendingIntent(screen: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("screen", screen)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return PendingIntent.getActivity(
            this,
            screen.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
