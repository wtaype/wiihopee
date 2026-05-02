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
    val registradoPor: String = "correo",
    val creacion: Timestamp = Timestamp.now(),
    val ultimaActividad: Timestamp = Timestamp.now(),
    val aceptoTerminos: Timestamp? = Timestamp.now(),
) {
    val nombreCompleto: String get() = "$nombre $apellidos".trim()
    val usuarioLimpio: String get() = usuario.lowercase().trim()

    fun toFirestore() = mapOf(
        "email" to email.lowercase().trim(),
        "usuario" to usuarioLimpio,
        "nombre" to nombre.trim(),
        "apellidos" to apellidos.trim(),
        "grupo" to grupo.lowercase().trim(),
        "genero" to genero,
        "rol" to rol,
        "activo" to activo,
        "estado" to estado,
        "uid" to uid,
        "foto" to foto,
        "avatar" to foto.orEmpty(),
        "terminos" to (aceptoTerminos != null),
        "plan" to "free",
        "segmento" to when (rol) {
            "gestor" -> "negocio"
            "empresa" -> "empresa"
            else -> "creador"
        },
        "verificado" to false,
        "registradoPor" to registradoPor,
        "creacion" to creacion,
        "creado" to creacion,
        "ultimaActividad" to ultimaActividad,
        "aceptoTerminos" to aceptoTerminos,
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
                registradoPor = data["registradoPor"] as? String ?: "correo",
                creacion = data["creacion"] as? Timestamp ?: Timestamp.now(),
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
