package com.wiihope.app.core.model

import com.wiihope.app.R

data class BibleBook(
    val id: Int,
    val name: String,
    val slug: String,
    val chapters: Int,
    val baseUrl: String,
    val fileName: String,
    val trackStart: Int,
) {
    fun chapterTrack(chapterIndex: Int) = AudioTrack(
        id = "bible-$slug-${chapterIndex + 1}",
        title = "Capitulo ${chapterIndex + 1}",
        artist = name,
        subtitle = "$name · Nuevo Testamento",
        source = TrackSource.Bible,
        url = "$baseUrl/${trackStart + chapterIndex}_${fileName}_${(chapterIndex + 1).toString().padStart(2, '0')}.mp3",
        artworkRes = R.drawable.jesus,
    )
}
