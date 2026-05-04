package com.wiihope.app.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class BlogPost(
    val id: String = "",
    val slug: String = "",
    val titulo: String = "",
    val resumen: String = "",
    val categoria: String = "",
    val autor: String = "",
    val imagen: String = "",
    val imagenTop: String = "",
    val imagenAlt: String = "",
    val contenidoMd: String = "",
    val contenido: String = "",
    val tiempoLectura: String = "",
    val pin: Boolean = false,
    val activo: Boolean = true,
    val likes: Long = 0L,
    val vistas: Long = 0L,
    val creado: Timestamp? = null,
) {
    val routeId: String get() = slug.ifBlank { id }
    val cover: String get() = imagenTop.ifBlank { imagen }
    val previewCover: String get() = imagen.ifBlank { imagenTop }

    companion object {
        fun fromFirestore(doc: DocumentSnapshot): BlogPost {
            val data = doc.data.orEmpty()
            return BlogPost(
                id = doc.id,
                slug = data["slug"] as? String ?: doc.id,
                titulo = data["titulo"] as? String ?: "Reflexion WiiHope",
                resumen = data["resumen"] as? String ?: "",
                categoria = data["categoria"] as? String ?: "Fe",
                autor = data["autor"] as? String ?: data["usuario"] as? String ?: "WiiHope",
                imagen = data["imagen"] as? String ?: data["portada"] as? String ?: "",
                imagenTop = data["imagenTop"] as? String ?: "",
                imagenAlt = data["imagenAlt"] as? String ?: data["titulo"] as? String ?: "Blog WiiHope",
                contenidoMd = data["contenidoMd"] as? String ?: "",
                contenido = data["contenido"] as? String ?: "",
                tiempoLectura = data["tiempoLectura"] as? String ?: "",
                pin = data["pin"] as? Boolean ?: false,
                activo = data["activo"] as? Boolean ?: true,
                likes = (data["likes"] as? Number)?.toLong() ?: 0L,
                vistas = (data["vistas"] as? Number)?.toLong() ?: 0L,
                creado = data["creado"] as? Timestamp,
            )
        }
    }
}
