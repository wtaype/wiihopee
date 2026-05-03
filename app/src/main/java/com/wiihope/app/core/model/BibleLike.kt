package com.wiihope.app.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

data class BibleLike(
    val id: String = "",
    val usuario: String = "",
    val email: String = "",
    val libro: String = "",
    val slug: String = "",
    val capitulo: Int = 1,
    val trackId: String = "",
    val titulo: String = "",
    val subtitulo: String = "",
    val creado: Timestamp? = null,
    val actualizado: Timestamp? = null,
) {
    fun toFirestore(isNew: Boolean) = buildMap<String, Any?> {
        put("usuario", usuario)
        put("email", email)
        put("libro", libro)
        put("slug", slug)
        put("capitulo", capitulo)
        put("trackId", trackId)
        put("titulo", titulo)
        put("subtitulo", subtitulo)
        if (isNew) put("creado", FieldValue.serverTimestamp()) else put("creado", creado ?: FieldValue.serverTimestamp())
        put("actualizado", FieldValue.serverTimestamp())
    }

    companion object {
        fun docId(usuario: String, slug: String, capitulo: Int): String =
            "${usuario.trim().lowercase()}_${slug.trim().lowercase()}_$capitulo"

        fun fromFirestore(doc: DocumentSnapshot): BibleLike {
            val data = doc.data.orEmpty()
            return BibleLike(
                id = doc.id,
                usuario = data["usuario"] as? String ?: "",
                email = data["email"] as? String ?: "",
                libro = data["libro"] as? String ?: "",
                slug = data["slug"] as? String ?: "",
                capitulo = (data["capitulo"] as? Number)?.toInt() ?: 1,
                trackId = data["trackId"] as? String ?: "",
                titulo = data["titulo"] as? String ?: "",
                subtitulo = data["subtitulo"] as? String ?: "",
                creado = data["creado"] as? Timestamp,
                actualizado = data["actualizado"] as? Timestamp,
            )
        }
    }
}
