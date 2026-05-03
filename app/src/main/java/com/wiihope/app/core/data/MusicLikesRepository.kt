package com.wiihope.app.core.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.MusicLike
import com.wiihope.app.core.model.UserProfile
import kotlinx.coroutines.tasks.await

class MusicLikesRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    private val collection get() = firestore.collection("musicaLikes")

    suspend fun load(usuario: String, forceServer: Boolean = false): List<MusicLike> {
        if (usuario.isBlank()) return emptyList()
        val snapshot = collection
            .whereEqualTo("usuario", usuario.trim().lowercase())
            .limit(80)
            .fastGet(forceServer)
        return snapshot.documents
            .map(MusicLike::fromFirestore)
            .sortedByDescending { it.actualizado?.seconds ?: it.creado?.seconds ?: 0L }
    }

    suspend fun save(profile: UserProfile, track: AudioTrack) {
        val like = MusicLike.fromTrack(profile, track)
        collection.document(like.id).set(like.toFirestore(isNew = true)).await()
    }

    suspend fun delete(profile: UserProfile, track: AudioTrack) {
        collection.document(MusicLike.docId(profile.usuario, track.id)).delete().await()
    }

    private suspend fun Query.fastGet(forceServer: Boolean) = if (forceServer) {
        get(Source.SERVER).await()
    } else {
        runCatching { get(Source.CACHE).await() }
            .getOrElse { get(Source.DEFAULT).await() }
            .let { cached -> if (cached.isEmpty) get(Source.DEFAULT).await() else cached }
    }
}
