package com.wiihope.app.feature.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wiihope.app.core.audio.MediaControllerHolder
import com.wiihope.app.core.audio.PlaybackState
import com.wiihope.app.core.data.AuthRepository
import com.wiihope.app.core.data.BibleCatalog
import com.wiihope.app.core.data.BibleLikesRepository
import com.wiihope.app.core.data.MusicLikesRepository
import com.wiihope.app.core.data.MusicRepository
import com.wiihope.app.core.data.QuoteRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.MusicLike
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.UserProfile
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WiiHopeViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val bibleLikesRepository = BibleLikesRepository()
    private val musicLikesRepository = MusicLikesRepository()
    private val musicRepository = MusicRepository()
    private val quoteRepository = QuoteRepository()
    private val mediaController = MediaControllerHolder(application.applicationContext)
    private var publicQuotesCursor: DocumentSnapshot? = null

    private val _uiState = MutableStateFlow(WiiHopeUiState(books = BibleCatalog.books()))
    val uiState: StateFlow<WiiHopeUiState> = _uiState.asStateFlow()
    val playbackState: StateFlow<PlaybackState> = mediaController.state

    init {
        mediaController.connect()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(hasAuthSession = authRepository.isLoggedIn)
            loadSessionNow()
            _uiState.value = _uiState.value.copy(booting = false)
            refreshMusic()
        }
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
        val needsGoogleProfile = profile == null && authRepository.isLoggedIn
        _uiState.value = _uiState.value.copy(
            profile = profile,
            hasAuthSession = profile != null,
            googlePending = needsGoogleProfile,
            googleEmail = if (needsGoogleProfile) authRepository.currentEmail.orEmpty() else "",
            googleName = if (needsGoogleProfile) authRepository.currentEmail?.substringBefore("@").orEmpty() else "",
        )
        if (profile != null) {
            refreshQuotes()
            refreshBibleLikes()
            refreshMusicLikes()
        }
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
                refreshBibleLikes()
                refreshMusicLikes()
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
                    refreshBibleLikes()
                    refreshMusicLikes()
                } else {
                    _uiState.value = _uiState.value.copy(
                        authLoading = false,
                        hasAuthSession = false,
                        googlePending = true,
                        googleEmail = authRepository.currentEmail.orEmpty(),
                        googleName = authRepository.currentEmail?.substringBefore("@").orEmpty(),
                        message = "Excelente, completa tu registro",
                    )
                }
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(hasAuthSession = false, authLoading = false, error = e.cleanMessage())
            }
        }
    }

    fun googleSignInFailed(message: String = "No pudimos iniciar con Google") {
        _uiState.value = _uiState.value.copy(authLoading = false, hasAuthSession = false, error = message)
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
                refreshBibleLikes()
                refreshMusicLikes()
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
                refreshBibleLikes()
                refreshMusicLikes()
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
        _uiState.value = _uiState.value.copy(
            profile = null,
            hasAuthSession = false,
            googlePending = false,
            publicQuotes = emptyList(),
            privateQuotes = emptyList(),
            quotesTotal = 0L,
            quotesHasMore = false,
            bibleLikes = emptyList(),
            musicLikes = emptyList(),
        )
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

    fun refreshMusicLikes(forceServer: Boolean = false) {
        val usuario = _uiState.value.profile?.usuarioLimpio.orEmpty()
        if (usuario.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(musicLikesLoading = true)
            runCatching { musicLikesRepository.load(usuario, forceServer) }
                .onSuccess { _uiState.value = _uiState.value.copy(musicLikes = it, musicLikesLoading = false) }
                .onFailure { _uiState.value = _uiState.value.copy(musicLikesLoading = false, error = it.cleanMessage()) }
        }
    }

    fun toggleMusicLike(track: AudioTrack) {
        val profile = _uiState.value.profile ?: return
        val docId = MusicLike.docId(profile.usuario, track.id)
        val current = _uiState.value.musicLikes
        val isLiked = current.any { it.id == docId }
        val optimistic = if (isLiked) {
            current.filterNot { it.id == docId }
        } else {
            listOf(MusicLike.fromTrack(profile, track)) + current.filterNot { it.id == docId }
        }
        _uiState.value = _uiState.value.copy(musicLikes = optimistic)
        viewModelScope.launch {
            runCatching {
                if (isLiked) {
                    musicLikesRepository.delete(profile, track)
                } else {
                    musicLikesRepository.save(profile, track)
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(musicLikes = current, error = it.cleanMessage())
            }
        }
    }

    fun refreshQuotes(forceServer: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(quotesLoading = true)
            runCatching {
                val page = quoteRepository.loadPublicQuotesPage(forceServer = forceServer)
                val total = runCatching { quoteRepository.countPublicQuotes() }.getOrDefault(_uiState.value.quotesTotal)
                page to total
            }.onSuccess { (page, total) ->
                publicQuotesCursor = page.lastDoc
                _uiState.value = _uiState.value.copy(
                    publicQuotes = page.quotes,
                    privateQuotes = emptyList(),
                    quotesTotal = total,
                    quotesHasMore = page.hasMore,
                    quotesLoading = false,
                    quotesLoadingMore = false,
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(quotesLoading = false, error = it.cleanMessage())
            }
        }
    }

    fun loadMoreQuotes() {
        if (_uiState.value.quotesLoadingMore || !_uiState.value.quotesHasMore) return
        val cursor = publicQuotesCursor ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(quotesLoadingMore = true)
            runCatching { quoteRepository.loadPublicQuotesPage(startAfter = cursor, forceServer = true) }
                .onSuccess { page ->
                    publicQuotesCursor = page.lastDoc ?: publicQuotesCursor
                    _uiState.value = _uiState.value.copy(
                        publicQuotes = _uiState.value.publicQuotes + page.quotes,
                        quotesHasMore = page.hasMore,
                        quotesLoadingMore = false,
                    )
                }
                .onFailure { _uiState.value = _uiState.value.copy(quotesLoadingMore = false, error = it.cleanMessage()) }
        }
    }

    fun refreshBibleLikes(forceServer: Boolean = false) {
        val usuario = _uiState.value.profile?.usuarioLimpio.orEmpty()
        if (usuario.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(bibleLikesLoading = true)
            runCatching { bibleLikesRepository.load(usuario, forceServer) }
                .onSuccess { _uiState.value = _uiState.value.copy(bibleLikes = it, bibleLikesLoading = false) }
                .onFailure { _uiState.value = _uiState.value.copy(bibleLikesLoading = false, error = it.cleanMessage()) }
        }
    }

    fun toggleBibleLike(book: BibleBook, chapter: Int, track: AudioTrack) {
        val profile = _uiState.value.profile ?: return
        val docId = com.wiihope.app.core.model.BibleLike.docId(profile.usuario, book.slug, chapter)
        val current = _uiState.value.bibleLikes
        val isLiked = current.any { it.id == docId }
        val optimisticLike = com.wiihope.app.core.model.BibleLike(
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
        val optimistic = if (isLiked) {
            current.filterNot { it.id == docId }
        } else {
            current.filterNot { it.id == docId } + optimisticLike
        }
        _uiState.value = _uiState.value.copy(bibleLikes = optimistic)
        viewModelScope.launch {
            runCatching {
                if (isLiked) {
                    bibleLikesRepository.delete(profile, book, chapter)
                } else {
                    bibleLikesRepository.save(profile, book, chapter, track)
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    bibleLikes = current,
                    error = it.cleanMessage(),
                )
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
        val current = _uiState.value.publicQuotes
        val updated = current.map { if (it.id == quote.id) it.copy(favorito = !it.favorito) else it }
            .sortedWith(compareByDescending<Quote> { it.favorito }.thenByDescending { it.creado?.seconds ?: 0L })
        _uiState.value = _uiState.value.copy(publicQuotes = updated)
        viewModelScope.launch {
            runCatching { quoteRepository.toggleFavorite(quote) }
                .onFailure { _uiState.value = _uiState.value.copy(publicQuotes = current, error = it.cleanMessage()) }
        }
    }

    fun deleteQuote(quote: Quote) {
        val current = _uiState.value.publicQuotes
        _uiState.value = _uiState.value.copy(
            publicQuotes = current.filterNot { it.id == quote.id },
            quotesTotal = (_uiState.value.quotesTotal - 1).coerceAtLeast(0L),
        )
        viewModelScope.launch {
            runCatching { quoteRepository.deleteQuote(quote) }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(message = "Cita eliminada")
                }
                .onFailure { _uiState.value = _uiState.value.copy(publicQuotes = current, quotesTotal = _uiState.value.quotesTotal + 1, error = it.cleanMessage()) }
        }
    }

    fun play(track: AudioTrack, queue: List<AudioTrack>) = mediaController.play(track, queue)
    fun togglePlayback() = mediaController.toggle()
    fun next() = mediaController.next()
    fun previous() = mediaController.previous()
    fun toggleLoopOne() = mediaController.toggleLoopOne()
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
