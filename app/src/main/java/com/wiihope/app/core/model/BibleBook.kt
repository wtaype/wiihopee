package com.wiihope.app.core.model

import com.wiihope.app.R

data class BibleBook(
    val id: Int,
    val name: String,
    val chapters: Int,
    val baseUrl: String,
    val fileName: String,
    val trackStart: Int,
) {
    fun chapterTrack(chapterIndex: Int) = AudioTrack(
        id = "bible-$id-${chapterIndex + 1}",
        title = "$name ${chapterIndex + 1}",
        artist = "Biblia Audio",
        subtitle = "Capitulo ${chapterIndex + 1}",
        source = TrackSource.Bible,
        url = "$baseUrl/${trackStart + chapterIndex}_${fileName}_${(chapterIndex + 1).toString().padStart(2, '0')}.mp3",
        artworkRes = R.drawable.jesus,
    )
}
