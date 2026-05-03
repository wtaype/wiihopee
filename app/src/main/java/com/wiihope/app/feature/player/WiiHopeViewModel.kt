package com.wiihope.app.feature.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wiihope.app.core.audio.MediaControllerHolder
import com.wiihope.app.core.audio.PlaybackState
import com.wiihope.app.core.data.AuthRepository
import com.wiihope.app.core.data.BibleCatalog
import com.wiihope.app.core.data.MusicRepository
import com.wiihope.app.core.data.QuoteRepository
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.UserProfile
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WiiHopeUiState(
    val booting: Boolean = true,
    val hasAuthSession: Boolean = false,
    val profile: UserProfile? = null,
    val books: List<BibleBook> = emptyList(),
    val music: List<AudioTrack> = emptyList(),
    val publicQuotes: List<Quote> = emptyList(),
    val privateQuotes: List<Quote> = emptyList(),
    val musicLoading: Boolean = false,
    val quotesLoading: Boolean = false,
    val authLoading: Boolean = false,
    val googlePending: Boolean = false,
    val googleEmail: String = "",
    val googleName: String = "",
    val message: String? = null,
    val error: String? = null,
) {
    val isLoggedIn: Boolean get() = profile != null || (hasAuthSession && !googlePending)
}

class WiiHopeViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val musicRepository = MusicRepository()
    private val quoteRepository = QuoteRepository()
    private val mediaController = MediaControllerHolder(application.applicationContext)

    private val _uiState = MutableStateFlow(WiiHopeUiState(books = BibleCatalog.books()))
    val uiState: StateFlow<WiiHopeUiState> = _uiState.asStateFlow()
    val playbackState: StateFlow<PlaybackState> = mediaController.state

    init {
        mediaController.connect()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(hasAuthSession = authRepository.isLoggedIn)
            loadSessionNow()
            delay(220)
            _uiState.value = _uiState.value.copy(booting = false)
        }
        refreshMusic()
        startProgressTicker()
    }

    fun consumeMessages() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    fun loadSession() {
        viewModelScope.launch {
            loadSessionNow()
        }
    }

    private suspend fun loadSessionNow() {
        if (!authRepository.isLoggedIn) {
            _uiState.value = _uiState.value.copy(profile = null, hasAuthSession = false)
            return
        }
        val profile = runCatching { authRepository.getProfile() }.getOrNull()
        _uiState.value = _uiState.value.copy(
            profile = profile,
            hasAuthSession = authRepository.isLoggedIn,
            googlePending = false,
        )
        if (profile != null) refreshQuotes()
    }

    fun login(emailOrUser: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authLoading = true, error = null)
            runCatching {
                authRepository.login(emailOrUser, password)
                authRepository.getProfile()
            }.onSuccess { profile ->
                _uiState.value = _uiState.value.copy(profile = profile, hasAuthSession = profile != null, authLoading = false, message = "Bienvenido")
                refreshQuotes()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(hasAuthSession = false, authLoading = false, error = e.cleanMessage())
            }
        }
    }

    fun loginWithGoogleIdToken(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authLoading = true, error = null)
            runCatching {
                authRepository.loginWithGoogleIdToken(idToken)
            }.onSuccess { profile ->
                if (profile != null) {
                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        hasAuthSession = true,
                        googlePending = false,
                        authLoading = false,
                        message = "Bienvenido",
                    )
                    refreshQuotes()
                } else {
                    _uiState.value = _uiState.value.copy(
                        authLoading = false,
                        hasAuthSession = false,
                        googlePending = true,
                        googleEmail = authRepository.currentEmail.orEmpty(),
                        googleName = authRepository.currentEmail?.substringBefore("@").orEmpty(),
                        message = "Completa tu perfil",
                    )
                }
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(hasAuthSession = false, authLoading = false, error = e.cleanMessage())
            }
        }
    }

    fun completeGoogleRegistration(usuario: String, password: String, rol: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authLoading = true, error = null)
            runCatching {
                authRepository.completeGoogleRegistration(usuario, password, rol)
            }.onSuccess { profile ->
                _uiState.value = _uiState.value.copy(
                    profile = profile,
                    hasAuthSession = true,
                    googlePending = false,
                    authLoading = false,
                    message = "Tu cuenta esta lista",
                )
                refreshQuotes()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(authLoading = false, error = e.cleanMessage())
            }
        }
    }

    fun register(email: String, usuario: String, nombre: String, apellidos: String, grupo: String, genero: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authLoading = true, error = null)
            runCatching {
                val cleanEmail = email.trim().lowercase()
                val cleanUser = usuario.trim().lowercase()
                require(cleanEmail.contains("@")) { "Ingresa un email valido" }
                require(cleanUser.length >= 3) { "El usuario necesita al menos 3 caracteres" }
                require(password.length >= 6) { "La contrasena necesita al menos 6 caracteres" }
                require(!authRepository.emailExists(cleanEmail)) { "Ese email ya existe" }
                require(!authRepository.userExists(cleanUser)) { "Ese usuario ya existe" }
                authRepository.register(
                    UserProfile.nuevo(cleanEmail, cleanUser, nombre, apellidos, grupo, genero, uid = ""),
                    password,
                )
                authRepository.getProfile()
            }.onSuccess { profile ->
                _uiState.value = _uiState.value.copy(profile = profile, hasAuthSession = profile != null, authLoading = false, message = "Cuenta creada")
                refreshQuotes()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(authLoading = false, error = e.cleanMessage())
            }
        }
    }

    fun recover(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authLoading = true, error = null)
            runCatching { authRepository.recover(email) }
                .onSuccess { _uiState.value = _uiState.value.copy(authLoading = false, message = "Revisa tu correo") }
                .onFailure { _uiState.value = _uiState.value.copy(authLoading = false, error = it.cleanMessage()) }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = _uiState.value.copy(profile = null, hasAuthSession = false, googlePending = false, publicQuotes = emptyList(), privateQuotes = emptyList())
    }

    fun updatePhoto(url: String) {
        val profile = _uiState.value.profile ?: return
        viewModelScope.launch {
            runCatching {
                authRepository.updateProfilePhoto(profile.usuario, url.ifBlank { null })
                authRepository.getProfile()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(profile = it, message = "Foto actualizada")
            }.onFailure {
                _uiState.value = _uiState.value.copy(error = it.cleanMessage())
            }
        }
    }

    fun refreshMusic() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(musicLoading = true)
            runCatching { musicRepository.loadMusic() }
                .onSuccess { _uiState.value = _uiState.value.copy(music = it, musicLoading = false) }
                .onFailure { _uiState.value = _uiState.value.copy(musicLoading = false, error = it.cleanMessage()) }
        }
    }

    fun refreshQuotes(forceServer: Boolean = false) {
        val email = _uiState.value.profile?.email.orEmpty()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(quotesLoading = true)
            runCatching {
                val public = quoteRepository.loadPublicQuotes(forceServer = forceServer)
                val private = if (email.isBlank()) emptyList() else quoteRepository.loadPrivateQuotes(email, forceServer = forceServer)
                public to private
            }.onSuccess { (public, private) ->
                _uiState.value = _uiState.value.copy(publicQuotes = public, privateQuotes = private, quotesLoading = false)
            }.onFailure {
                _uiState.value = _uiState.value.copy(quotesLoading = false, error = it.cleanMessage())
            }
        }
    }

    fun saveQuote(quote: Quote) {
        val profile = _uiState.value.profile ?: return
        viewModelScope.launch {
            runCatching {
                quoteRepository.saveQuote(
                    quote.copy(
                        usuario = profile.usuario,
                        email = profile.email,
                        nombreShow = profile.nombreCompleto.ifBlank { profile.usuario },
                        creado = quote.creado ?: Timestamp.now(),
                    )
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(message = "Cita guardada")
                refreshQuotes()
            }.onFailure {
                _uiState.value = _uiState.value.copy(error = it.cleanMessage())
            }
        }
    }

    fun toggleQuoteFavorite(quote: Quote) {
        viewModelScope.launch {
            runCatching { quoteRepository.toggleFavorite(quote) }
                .onSuccess { refreshQuotes() }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.cleanMessage()) }
        }
    }

    fun deleteQuote(quote: Quote) {
        viewModelScope.launch {
            runCatching { quoteRepository.deleteQuote(quote) }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(message = "Cita eliminada")
                    refreshQuotes()
                }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.cleanMessage()) }
        }
    }

    fun play(track: AudioTrack, queue: List<AudioTrack>) = mediaController.play(track, queue)
    fun togglePlayback() = mediaController.toggle()
    fun next() = mediaController.next()
    fun previous() = mediaController.previous()
    fun seekTo(positionMs: Long) = mediaController.seekTo(positionMs)

    private fun startProgressTicker() {
        viewModelScope.launch {
            while (true) {
                mediaController.refresh()
                delay(900)
            }
        }
    }

    override fun onCleared() {
        mediaController.release()
        super.onCleared()
    }

    private fun Throwable.cleanMessage(): String = message?.replace("An internal error has occurred. ", "") ?: "Algo no salio bien"
}
