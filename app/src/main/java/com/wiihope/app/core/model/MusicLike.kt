package com.wiihope.app.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

data class MusicLike(
    val id: String = "",
    val usuario: String = "",
    val email: String = "",
    val trackId: String = "",
    val slug: String = "",
    val titulo: String = "",
    val artista: String = "",
    val subtitulo: String = "",
    val url: String = "",
    val artworkUrl: String = "",
    val creado: Timestamp? = null,
    val actualizado: Timestamp? = null,
) {
    fun toFirestore(isNew: Boolean) = buildMap<String, Any?> {
        put("usuario", usuario)
        put("email", email)
        put("trackId", trackId)
        put("slug", slug)
        put("titulo", titulo)
        put("artista", artista)
        put("subtitulo", subtitulo)
        put("url", url)
        put("artworkUrl", artworkUrl)
        if (isNew) put("creado", FieldValue.serverTimestamp()) else put("creado", creado ?: FieldValue.serverTimestamp())
        put("actualizado", FieldValue.serverTimestamp())
    }

    fun toAudioTrack() = AudioTrack(
        id = trackId,
        title = titulo.ifBlank { "Cancion" },
        artist = artista.ifBlank { "WiiHope" },
        subtitle = subtitulo,
        url = url,
        source = TrackSource.Music,
        artworkUrl = artworkUrl.ifBlank { null },
    )

    companion object {
        fun docId(usuario: String, trackId: String): String =
            "${usuario.trim().lowercase()}_${trackId.trim().lowercase()}"

        fun slug(title: String): String =
            title.lowercase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .replace(Regex("[^a-z0-9]+"), "-")
                .trim('-')

        fun fromTrack(profile: UserProfile, track: AudioTrack) = MusicLike(
            id = docId(profile.usuario, track.id),
            usuario = profile.usuarioLimpio,
            email = profile.email,
            trackId = track.id,
            slug = slug(track.title),
            titulo = track.title,
            artista = track.artist,
            subtitulo = track.subtitle,
            url = track.url,
            artworkUrl = track.artworkUrl.orEmpty(),
        )

        fun fromFirestore(doc: DocumentSnapshot): MusicLike {
            val data = doc.data.orEmpty()
            return MusicLike(
                id = doc.id,
                usuario = data["usuario"] as? String ?: "",
                email = data["email"] as? String ?: "",
                trackId = data["trackId"] as? String ?: "",
                slug = data["slug"] as? String ?: "",
                titulo = data["titulo"] as? String ?: "",
                artista = data["artista"] as? String ?: "",
                subtitulo = data["subtitulo"] as? String ?: "",
                url = data["url"] as? String ?: "",
                artworkUrl = data["artworkUrl"] as? String ?: "",
                creado = data["creado"] as? Timestamp,
                actualizado = data["actualizado"] as? Timestamp,
            )
        }
    }
}
