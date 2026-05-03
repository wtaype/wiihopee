package com.wiihope.app.core.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class UserProfile(
    val email: String = "",
    val usuario: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val grupo: String = "",
    val genero: String = "masculino",
    val rol: String = "smile",
    val activo: Boolean = true,
    val estado: String = "activo",
    val uid: String = "",
    val foto: String? = null,
    val tema: String = "Oro|#FFC107",
    val registradoPor: String = "correo",
    val creacion: Timestamp = Timestamp.now(),
    val ultimaActividad: Timestamp = Timestamp.now(),
    val aceptoTerminos: Timestamp? = Timestamp.now(),
) {
    val nombreCompleto: String get() = "$nombre $apellidos".trim()
    val usuarioLimpio: String get() = usuario.lowercase().trim()

    fun toFirestore() = mapOf(
        "apellidos" to apellidos.trim(),
        "avatar" to foto.orEmpty(),
        "bio" to "",
        "creado" to creacion,
        "email" to email.lowercase().trim(),
        "estado" to estado,
        "nombre" to nombre.trim(),
        "plan" to "free",
        "registradoPor" to registradoPor,
        "rol" to rol,
        "segmento" to when (rol) {
            "gestor" -> "negocio"
            "empresa" -> "empresa"
            else -> "creador"
        },
        "tema" to tema,
        "terminos" to (aceptoTerminos != null),
        "uid" to uid,
        "usuario" to usuarioLimpio,
        "verificado" to false,
    )

    companion object {
        fun fromFirestore(doc: DocumentSnapshot): UserProfile {
            val data = doc.data.orEmpty()
            return UserProfile(
                email = data["email"] as? String ?: "",
                usuario = doc.id,
                nombre = data["nombre"] as? String ?: "",
                apellidos = data["apellidos"] as? String ?: "",
                grupo = data["grupo"] as? String ?: "genial",
                genero = data["genero"] as? String ?: "masculino",
                rol = data["rol"] as? String ?: "smile",
                activo = data["activo"] as? Boolean ?: true,
                estado = data["estado"] as? String ?: if (data["activo"] as? Boolean == false) "pendiente" else "activo",
                uid = data["uid"] as? String ?: "",
                foto = data["foto"] as? String ?: data["avatar"] as? String,
                tema = data["tema"] as? String ?: "Oro|#FFC107",
                registradoPor = data["registradoPor"] as? String ?: "correo",
                creacion = data["creacion"] as? Timestamp ?: data["creado"] as? Timestamp ?: Timestamp.now(),
                ultimaActividad = data["ultimaActividad"] as? Timestamp ?: Timestamp.now(),
                aceptoTerminos = data["aceptoTerminos"] as? Timestamp,
            )
        }

        fun nuevo(
            email: String,
            usuario: String,
            nombre: String,
            apellidos: String,
            grupo: String,
            genero: String,
            uid: String,
        ): UserProfile {
            val now = Timestamp.now()
            return UserProfile(
                email = email.lowercase().trim(),
                usuario = usuario.lowercase().trim(),
                nombre = nombre.trim(),
                apellidos = apellidos.trim(),
                grupo = grupo.lowercase().trim(),
                genero = genero,
                uid = uid,
                registradoPor = "correo",
                creacion = now,
                ultimaActividad = now,
                aceptoTerminos = now,
            )
        }
    }
}
