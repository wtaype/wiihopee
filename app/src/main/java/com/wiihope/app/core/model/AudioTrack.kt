package com.wiihope.app.core.model

enum class TrackSource { Prayer, Bible, Music }

data class AudioTrack(
    val id: String,
    val title: String,
    val artist: String,
    val url: String,
    val source: TrackSource,
    val subtitle: String = "",
    val artworkRes: Int? = null,
    val artworkUrl: String? = null,
)
