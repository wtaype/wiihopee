package com.wiihope.app.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

data class Quote(
    val id: String = "",
    val cita: String = "",
    val libro: String = "",
    val nombreShow: String = "",
    val usuario: String = "",
    val email: String = "",
    val categoria: String = "Fe",
    val favorito: Boolean = false,
    val publico: Boolean = true,
    val creado: Timestamp? = null,
    val actualizado: Timestamp? = null,
) {
    fun toFirestore(isNew: Boolean = id.isBlank()) = buildMap<String, Any?> {
        put("cita", cita)
        put("libro", libro)
        put("nombreShow", nombreShow)
        put("usuario", usuario)
        put("email", email)
        put("categoria", categoria.ifBlank { "Fe" })
        put("favorito", favorito)
        put("publico", publico)
        if (isNew) put("creado", FieldValue.serverTimestamp()) else put("creado", creado ?: FieldValue.serverTimestamp())
        put("actualizado", FieldValue.serverTimestamp())
    }

    companion object {
        fun fromFirestore(doc: DocumentSnapshot): Quote {
            val data = doc.data.orEmpty()
            return Quote(
                id = doc.id,
                cita = data["cita"] as? String ?: "",
                libro = data["libro"] as? String ?: "",
                nombreShow = data["nombreShow"] as? String ?: "",
                usuario = data["usuario"] as? String ?: "",
                email = data["email"] as? String ?: "",
                categoria = data["categoria"] as? String ?: "Fe",
                favorito = data["favorito"] as? Boolean ?: false,
                publico = data["publico"] as? Boolean ?: true,
                creado = data["creado"] as? Timestamp,
                actualizado = data["actualizado"] as? Timestamp,
            )
        }
    }
}
