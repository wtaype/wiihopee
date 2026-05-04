package com.wiihope.app.core.data

import com.wiihope.app.R
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.TrackSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class MusicRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun loadMusic(limit: Long = 100): List<AudioTrack> {
        val snapshot = firestore.collection("wimusica")
            .orderBy("creado", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val url = doc.getString("url").orEmpty()
            if (url.isBlank()) return@mapNotNull null
            AudioTrack(
                id = "music-${doc.id}",
                title = doc.getString("nombre").orEmpty().ifBlank { "Cancion" },
                artist = doc.getString("cantante").orEmpty().ifBlank { "WiiHope" },
                subtitle = doc.getString("tag").orEmpty(),
                url = url,
                source = TrackSource.Music,
                artworkRes = R.drawable.album,
                artworkUrl = doc.firstImageUrl(),
            )
        }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.firstImageUrl(): String? {
        val fields = listOf(
            "portada",
            "portadaUrl",
            "artworkUrl",
            "artwork",
            "thumbnailUrl",
            "thumbnail",
            "coverUrl",
            "cover",
            "imageUrl",
            "image",
            "imagenUrl",
            "imagen",
            "fotoUrl",
            "foto",
        )
        return fields.firstNotNullOfOrNull { field -> getString(field)?.trim()?.takeIf { it.isNotBlank() } }
    }
}
