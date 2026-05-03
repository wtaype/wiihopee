package com.wiihope.app.core.data

import com.wiihope.app.core.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    val currentEmail: String? get() = auth.currentUser?.email
    val currentUid: String? get() = auth.currentUser?.uid
    val isLoggedIn: Boolean get() = auth.currentUser != null

    suspend fun login(emailOrUser: String, password: String) {
        val clean = emailOrUser.trim().lowercase()
        val email = if ("@" in clean) clean else getEmailByUser(clean) ?: error("Usuario no encontrado")
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(profile: UserProfile, password: String) {
        val credential = auth.createUserWithEmailAndPassword(profile.email, password).await()
        val saved = profile.copy(uid = credential.user?.uid.orEmpty())
        firestore.collection("smiles").document(saved.usuarioLimpio).set(saved.toFirestore()).await()
    }

    suspend fun recover(email: String) {
        auth.sendPasswordResetEmail(email.trim().lowercase()).await()
    }

    suspend fun getProfile(): UserProfile? {
        val uid = currentUid
        if (!uid.isNullOrBlank()) {
            getProfileByUid(uid)?.let { return it }
        }
        val email = currentEmail ?: return null
        val query = firestore.collection("smiles").whereEqualTo("email", email.lowercase()).limit(1).fastGet()
        return query.documents.firstOrNull()?.let(UserProfile::fromFirestore)
    }

    suspend fun loginWithGoogleIdToken(idToken: String): UserProfile? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val uid = result.user?.uid.orEmpty()
        val profile = getProfileByUid(uid)
        if (profile != null) {
            require(profile.estado != "pendiente" && profile.activo) { "Tu cuenta esta pendiente de activacion." }
        }
        return profile
    }

    suspend fun completeGoogleRegistration(usuario: String, password: String, rol: String): UserProfile {
        val user = auth.currentUser ?: error("Vuelve a iniciar con Google")
        val cleanUser = usuario.trim().lowercase()
        val email = user.email?.trim()?.lowercase().orEmpty()
        require(email.contains("@")) { "Google no devolvio un email valido" }
        require(cleanUser.matches(Regex("[a-z0-9_-]{4,}"))) { "Usuario minimo 4 caracteres, solo letras, numeros, _ o -" }
        require(password.length >= 6) { "La contrasena necesita al menos 6 caracteres" }
        require(!userExists(cleanUser)) { "Ese usuario ya existe" }
        require(!emailExists(email)) { "Ese email ya esta registrado" }

        val linked = runCatching {
            user.linkWithCredential(EmailAuthProvider.getCredential(email, password)).await()
        }.isSuccess
        if (!linked) {
            runCatching { user.updatePassword(password).await() }
        }

        val parts = user.displayName.orEmpty().trim().split(Regex("\\s+")).filter { it.isNotBlank() }
        val now = Timestamp.now()
        val profile = UserProfile(
            email = email,
            usuario = cleanUser,
            nombre = parts.firstOrNull() ?: "Usuario",
            apellidos = parts.drop(1).joinToString(" "),
            grupo = "wiihope",
            genero = "masculino",
            rol = rol.ifBlank { "smile" },
            activo = true,
            estado = "activo",
            uid = user.uid,
            foto = user.photoUrl?.toString(),
            tema = "Oro|#FFC107",
            registradoPor = "google",
            creacion = now,
            ultimaActividad = now,
            aceptoTerminos = now,
        )
        firestore.collection("smiles").document(profile.usuarioLimpio).set(profile.toFirestore()).await()
        return profile
    }

    suspend fun updateProfilePhoto(usuario: String, foto: String?) {
        firestore.collection("smiles").document(usuario.lowercase().trim()).update(
            mapOf("avatar" to foto)
        ).await()
    }

    suspend fun emailExists(email: String): Boolean {
        return firestore.collection("smiles").whereEqualTo("email", email.trim().lowercase()).limit(1).get().await().documents.isNotEmpty()
    }

    suspend fun userExists(usuario: String): Boolean {
        return firestore.collection("smiles").document(usuario.trim().lowercase()).get().await().exists()
    }

    fun logout() = auth.signOut()

    private suspend fun getEmailByUser(usuario: String): String? {
        val doc = firestore.collection("smiles").document(usuario.trim().lowercase()).get().await()
        return doc.getString("email")
    }

    private suspend fun getProfileByUid(uid: String): UserProfile? {
        val query = firestore.collection("smiles").whereEqualTo("uid", uid).limit(1).fastGet()
        return query.documents.firstOrNull()?.let(UserProfile::fromFirestore)
    }

    private suspend fun Query.fastGet() =
        runCatching { get(Source.CACHE).await() }
            .getOrElse { get(Source.DEFAULT).await() }
            .let { cached -> if (cached.isEmpty) get(Source.DEFAULT).await() else cached }
}
