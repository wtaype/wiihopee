package com.wiihope.app.core.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.BibleLike
import com.wiihope.app.core.model.UserProfile
import kotlinx.coroutines.tasks.await

class BibleLikesRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    private val collection get() = firestore.collection("bibliaLikes")

    suspend fun load(usuario: String, forceServer: Boolean = false): List<BibleLike> {
        if (usuario.isBlank()) return emptyList()
        val snapshot = collection
            .whereEqualTo("usuario", usuario.trim().lowercase())
            .limit(80)
            .fastGet(forceServer)
        return snapshot.documents
            .map(BibleLike::fromFirestore)
            .sortedBy { it.actualizado?.seconds ?: it.creado?.seconds ?: 0L }
    }

    suspend fun save(profile: UserProfile, book: BibleBook, chapter: Int, track: AudioTrack) {
        val docId = BibleLike.docId(profile.usuario, book.slug, chapter)
        val like = BibleLike(
            id = docId,
            usuario = profile.usuarioLimpio,
            email = profile.email,
            libro = book.name,
            slug = book.slug,
            capitulo = chapter,
            trackId = track.id,
            titulo = "Capitulo $chapter",
            subtitulo = book.name,
        )
        collection.document(docId).set(like.toFirestore(isNew = true)).await()
    }

    suspend fun delete(profile: UserProfile, book: BibleBook, chapter: Int) {
        val docId = BibleLike.docId(profile.usuario, book.slug, chapter)
        collection.document(docId).delete().await()
    }

    private suspend fun Query.fastGet(forceServer: Boolean) = if (forceServer) {
        get(Source.SERVER).await()
    } else {
        runCatching { get(Source.CACHE).await() }
            .getOrElse { get(Source.DEFAULT).await() }
            .let { cached -> if (cached.isEmpty) get(Source.DEFAULT).await() else cached }
    }
}
