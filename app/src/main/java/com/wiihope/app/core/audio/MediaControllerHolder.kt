package com.wiihope.app.core.audio

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.wiihope.app.R
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.TrackSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PlaybackState(
    val current: AudioTrack? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val loopOne: Boolean = false,
)

class MediaControllerHolder(private val context: Context) {
    private var controller: MediaController? = null
    private var queue: List<AudioTrack> = emptyList()

    private val _state = MutableStateFlow(PlaybackState())
    val state: StateFlow<PlaybackState> = _state

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) = publish(player)
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            controller?.let(::publish)
        }
    }

    fun connect(onReady: () -> Unit = {}) {
        if (controller != null) {
            onReady()
            return
        }
        val token = SessionToken(context, ComponentName(context, WiiHopePlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener(
            {
                controller = future.get().also {
                    it.addListener(listener)
                    publish(it)
                }
                onReady()
            },
            ContextCompat.getMainExecutor(context),
        )
    }

    fun play(track: AudioTrack, tracks: List<AudioTrack>) {
        connect {
            val activeQueue = tracks.ifEmpty { listOf(track) }
            queue = activeQueue
            val startIndex = activeQueue.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
            controller?.run {
                setMediaItems(activeQueue.map { it.toMediaItem() }, startIndex, C.TIME_UNSET)
                prepare()
                play()
                publish(this)
            }
        }
    }

    fun toggle() = controller?.run {
        if (isPlaying) pause() else play()
        publish(this)
    } ?: Unit

    fun seekTo(positionMs: Long) {
        controller?.seekTo(positionMs)
    }

    fun next() {
        controller?.seekToNextMediaItem()
    }

    fun previous() {
        controller?.seekToPreviousMediaItem()
    }

    fun toggleLoopOne() {
        controller?.run {
            repeatMode = if (repeatMode == Player.REPEAT_MODE_ONE) {
                Player.REPEAT_MODE_OFF
            } else {
                Player.REPEAT_MODE_ONE
            }
            publish(this)
        }
    }

    fun refresh() {
        controller?.let(::publish)
    }

    fun release() {
        controller?.removeListener(listener)
        controller?.release()
        controller = null
    }

    private fun publish(player: Player) {
        val mediaItem = player.currentMediaItem
        val mediaId = mediaItem?.mediaId
        val current = queue.firstOrNull { it.id == mediaId } ?: mediaItem?.toAudioTrack()
        _state.value = PlaybackState(
            current = current,
            isPlaying = player.isPlaying,
            positionMs = player.currentPosition.coerceAtLeast(0L),
            durationMs = player.duration.takeIf { it > 0 } ?: 0L,
            loopOne = player.repeatMode == Player.REPEAT_MODE_ONE,
        )
    }

    private fun AudioTrack.toMediaItem(): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setTitle(title)
            .setArtist(artist)
            .setDescription(subtitle)
            .setArtworkUri(artworkUrl?.let(Uri::parse))
            .setExtras(Bundle().apply { putString("source", source.name) })
            .build()
        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(url)
            .setMediaMetadata(metadata)
            .build()
    }

    private fun MediaItem.toAudioTrack(): AudioTrack {
        val source = mediaMetadata.extras?.getString("source")
            ?.let { runCatching { TrackSource.valueOf(it) }.getOrNull() }
            ?: TrackSource.Music
        return AudioTrack(
            id = mediaId,
            title = mediaMetadata.title?.toString().orEmpty().ifBlank { "WiiHope" },
            artist = mediaMetadata.artist?.toString().orEmpty().ifBlank { "WiiHope" },
            subtitle = mediaMetadata.description?.toString().orEmpty(),
            url = localConfiguration?.uri?.toString().orEmpty(),
            source = source,
            artworkRes = when (source) {
                TrackSource.Prayer, TrackSource.Bible -> R.drawable.jesus
                TrackSource.Music -> R.drawable.album
            },
            artworkUrl = mediaMetadata.artworkUri?.toString(),
        )
    }
}
