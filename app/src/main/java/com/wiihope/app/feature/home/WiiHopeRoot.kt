package com.wiihope.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wiihope.app.R
import com.wiihope.app.core.app.Wii
import com.wiihope.app.core.audio.PlaybackState
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.Quote
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.saludar
import com.wiihope.app.ui.components.wiDia
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText
import com.wiihope.app.ui.theme.premiumBackground
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

private enum class AuthMode { Login, Register, Recover }
private enum class MainTab(val label: String) { Oracion("Oracion"), Biblia("Biblia"), Citas("Citas"), Musica("Musica"), Ajustes("Ajustes") }

@Composable
fun WiiHopeRoot(viewModel: WiiHopeViewModel, launchScreen: String?, onGoogleClick: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val playback by viewModel.playbackState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.message, state.error) {
        val message = state.error ?: state.message
        if (message != null) {
            snackbar.showSnackbar(message)
            viewModel.consumeMessages()
        }
    }

    Box(Modifier.fillMaxSize().premiumBackground()) {
        when {
            state.booting -> SplashScreen()
            !state.isLoggedIn -> AuthShell(
                state = state,
                onLogin = viewModel::login,
                onRegister = viewModel::register,
                onRecover = viewModel::recover,
                onGoogleClick = onGoogleClick,
                onCompleteGoogle = viewModel::completeGoogleRegistration,
            )
            else -> MainShell(
                state = state,
                playback = playback,
                launchScreen = launchScreen,
                viewModel = viewModel,
                snackbar = snackbar,
            )
        }
        SnackbarHost(hostState = snackbar, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painterResource(R.drawable.inicio), contentDescription = "WiiHope", modifier = Modifier.size(168.dp), contentScale = ContentScale.Fit)
        Text(Wii.app, style = WiText.h1, modifier = Modifier.padding(top = 20.dp))
        Text(Wii.desc, style = WiText.body, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        CircularProgressIndicator(color = WiCss.primary, modifier = Modifier.padding(top = 28.dp))
    }
}

@Composable
private fun AuthShell(
    state: com.wiihope.app.feature.player.WiiHopeUiState,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String, String, String, String) -> Unit,
    onRecover: (String) -> Unit,
    onGoogleClick: () -> Unit,
    onCompleteGoogle: (String, String, String) -> Unit,
) {
    var mode by remember { mutableStateOf(AuthMode.Login) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.75f) {
                Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(92.dp).clip(CircleShape))
                Text(Wii.app, style = WiText.h1, modifier = Modifier.fillMaxWidth().padding(top = 10.dp), textAlign = TextAlign.Center)
                Text("Tu espacio de fe, audio y esperanza", style = WiText.body, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
        item {
            if (state.googlePending) {
                GoogleCompleteCard(state.authLoading, state.googleEmail, onCompleteGoogle)
            } else {
                when (mode) {
                    AuthMode.Login -> LoginCard(state.authLoading, onLogin, onGoogleClick, onRegisterClick = { mode = AuthMode.Register }, onRecoverClick = { mode = AuthMode.Recover })
                    AuthMode.Register -> RegisterCard(state.authLoading, onRegister, onGoogleClick, onLoginClick = { mode = AuthMode.Login })
                    AuthMode.Recover -> RecoverCard(state.authLoading, onRecover, onBack = { mode = AuthMode.Login })
                }
            }
        }
    }
}

@Composable
private fun LoginCard(loading: Boolean, onLogin: (String, String) -> Unit, onGoogleClick: () -> Unit, onRegisterClick: () -> Unit, onRecoverClick: () -> Unit) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Iniciar sesion", style = WiText.h2)
        WiButton("Continuar con Google", onGoogleClick, Modifier.fillMaxWidth().padding(top = 14.dp), Icons.Rounded.AccountCircle, loading, color = WiCss.black)
        Text("o usa tu email", style = WiText.tiny, modifier = Modifier.fillMaxWidth().padding(top = 12.dp), textAlign = TextAlign.Center)
        WiField(user, { user = it.replace(" ", "").lowercase() }, "Email o usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 14.dp))
        WiField(pass, { pass = it }, "Contrasena", leadingIcon = Icons.Rounded.Lock, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.padding(top = 10.dp))
        WiButton("Entrar", { onLogin(user, pass) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.AutoMirrored.Rounded.Login, loading)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onRecoverClick) { Text("Olvide mi contrasena") }
            TextButton(onClick = onRegisterClick) { Text("Crear cuenta") }
        }
    }
}

@Composable
private fun RegisterCard(loading: Boolean, onRegister: (String, String, String, String, String, String, String) -> Unit, onGoogleClick: () -> Unit, onLoginClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var grupo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("masculino") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Registro", style = WiText.h2)
        WiButton("Registrarme con Google", onGoogleClick, Modifier.fillMaxWidth().padding(top = 12.dp), Icons.Rounded.AccountCircle, loading, color = WiCss.black)
        Text("o crea tu acceso con email", style = WiText.tiny, modifier = Modifier.fillMaxWidth().padding(top = 12.dp), textAlign = TextAlign.Center)
        WiField(email, { email = it.replace(" ", "").lowercase() }, "Email", leadingIcon = Icons.Rounded.Mail, modifier = Modifier.padding(top = 12.dp))
        WiField(usuario, { usuario = it.replace(" ", "").lowercase() }, "Usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 10.dp))
        WiField(nombre, { nombre = it }, "Nombre", modifier = Modifier.padding(top = 10.dp))
        WiField(apellidos, { apellidos = it }, "Apellidos", modifier = Modifier.padding(top = 10.dp))
        WiField(grupo, { grupo = it }, "Grupo", modifier = Modifier.padding(top = 10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 10.dp)) {
            items(listOf("masculino", "femenino")) { item ->
                AssistChip(onClick = { genero = item }, label = { Text(item.replaceFirstChar { it.uppercase() }) }, leadingIcon = if (genero == item) ({ Icon(Icons.Rounded.Favorite, null) }) else null)
            }
        }
        WiField(pass, { pass = it }, "Contrasena", visualTransformation = PasswordVisualTransformation(), leadingIcon = Icons.Rounded.Lock, modifier = Modifier.padding(top = 10.dp))
        WiField(pass2, { pass2 = it }, "Confirmar contrasena", visualTransformation = PasswordVisualTransformation(), leadingIcon = Icons.Rounded.Lock, modifier = Modifier.padding(top = 10.dp))
        WiButton("Crear cuenta", { if (pass == pass2) onRegister(email, usuario, nombre, apellidos, grupo, genero, pass) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.Rounded.Person, loading)
        TextButton(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) { Text("Ya tengo cuenta", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
    }
}

@Composable
private fun GoogleCompleteCard(
    loading: Boolean,
    email: String,
    onComplete: (String, String, String) -> Unit,
) {
    var usuario by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var rol by remember { mutableStateOf("smile") }
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Casi listo", style = WiText.h2)
        Text(email.ifBlank { "Completa tu cuenta de Google" }, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
        WiField(usuario, { usuario = it.lowercase().replace(" ", "").filter { c -> c.isLetterOrDigit() || c == '_' || c == '-' } }, "Usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 14.dp))
        WiField(pass, { pass = it }, "Crea una contrasena", visualTransformation = PasswordVisualTransformation(), leadingIcon = Icons.Rounded.Lock, modifier = Modifier.padding(top = 10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 12.dp)) {
            items(listOf("smile", "gestor")) { item ->
                AssistChip(
                    onClick = { rol = item },
                    label = { Text(if (item == "smile") "Creador" else "Negocio") },
                    leadingIcon = if (rol == item) ({ Icon(Icons.Rounded.CheckCircle, null, modifier = Modifier.size(16.dp)) }) else null,
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Text("Acepto los terminos y condiciones", style = WiText.small)
        }
        WiButton(
            "Completar registro",
            { if (accepted) onComplete(usuario, pass, rol) },
            Modifier.fillMaxWidth().padding(top = 12.dp),
            Icons.AutoMirrored.Rounded.Login,
            loading,
        )
    }
}

@Composable
private fun RecoverCard(loading: Boolean, onRecover: (String) -> Unit, onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    GlassCard(Modifier.fillMaxWidth()) {
        Text("Recuperar acceso", style = WiText.h2)
        Text("Te enviaremos un enlace para restablecer tu contrasena.", style = WiText.body, modifier = Modifier.padding(top = 8.dp))
        WiField(email, { email = it.replace(" ", "").lowercase() }, "Email", leadingIcon = Icons.Rounded.Mail, modifier = Modifier.padding(top = 12.dp))
        WiButton("Enviar enlace", { onRecover(email) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.Rounded.Mail, loading)
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
    }
}

@Composable
private fun MainShell(
    state: com.wiihope.app.feature.player.WiiHopeUiState,
    playback: PlaybackState,
    launchScreen: String?,
    viewModel: WiiHopeViewModel,
    snackbar: SnackbarHostState,
) {
    var tab by remember { mutableIntStateOf(0) }
    LaunchedEffect(launchScreen) {
        tab = when (launchScreen) {
            "bible" -> 1
            "quotes" -> 2
            "music", "player" -> 3
            "settings" -> 4
            else -> tab
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            if (tab == 2) QuoteFab(viewModel)
        },
        bottomBar = {
            Column {
                MiniPlayer(playback, viewModel::togglePlayback, viewModel::previous, viewModel::next, viewModel::seekTo)
                NavigationBar(containerColor = WiCss.white.copy(alpha = 0.56f)) {
                    val icons = listOf(Icons.Rounded.VolunteerActivism, Icons.Rounded.Book, Icons.Rounded.Favorite, Icons.Rounded.Headphones, Icons.Rounded.Settings)
                    MainTab.entries.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = { tab = index },
                            icon = { Icon(icons[index], contentDescription = item.label, modifier = Modifier.size(20.dp)) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = WiCss.primary,
                                selectedTextColor = WiCss.primary,
                                indicatorColor = WiCss.gold.copy(alpha = 0.36f),
                                unselectedIconColor = WiCss.gray,
                                unselectedTextColor = WiCss.gray,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).premiumBackground()) {
            when (MainTab.entries[tab]) {
                MainTab.Oracion -> PrayerScreen(state.profile?.nombre.orEmpty())
                MainTab.Biblia -> BibleScreen(state.books, viewModel::play)
                MainTab.Citas -> QuotesScreen(state.publicQuotes, state.privateQuotes, state.quotesLoading, viewModel)
                MainTab.Musica -> MusicScreen(state.music, state.musicLoading, viewModel::refreshMusic, viewModel::play)
                MainTab.Ajustes -> SettingsScreen(state, viewModel)
            }
        }
    }
}

@Composable
private fun TopHero(title: String, subtitle: String, imageRes: Int = R.drawable.jesus) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.80f) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(imageRes), contentDescription = title, modifier = Modifier.size(92.dp).clip(RoundedCornerShape(24.dp)), contentScale = ContentScale.Crop)
            Column(Modifier.padding(start = 16.dp).weight(1f)) {
                Text(title, style = WiText.h2)
                Text(subtitle, style = WiText.body)
            }
        }
    }
}

@Composable
private fun PrayerScreen(name: String) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { TopHero("${saludar()}${if (name.isBlank()) "" else ", $name"}", wiDia()) }
        item {
            GlassCard(Modifier.fillMaxWidth()) {
                Text("Padre Nuestro", style = WiText.h2)
                Text(
                    "Padre nuestro, que estas en el cielo,\nsantificado sea tu Nombre;\nvenga a nosotros tu reino;\nhagase tu voluntad\nen la tierra como en el cielo.\n\nDanos hoy nuestro pan de cada dia;\nperdona nuestras ofensas,\ncomo tambien nosotros perdonamos\na los que nos ofenden;\nno nos dejes caer en la tentacion,\ny libranos del mal.",
                    style = WiText.body,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun BibleScreen(books: List<BibleBook>, onPlay: (AudioTrack, List<AudioTrack>) -> Unit) {
    var selected by remember { mutableIntStateOf(0) }
    val book = books.getOrNull(selected)
    val queue = book?.let { List(it.chapters) { chapter -> it.chapterTrack(chapter) } }.orEmpty()
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { TopHero("Biblia Audio", "Nuevo Testamento organizado por libros y capitulos") }
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.70f) {
                Text(book?.name ?: "Selecciona un libro", style = WiText.h2)
                Text("${book?.chapters ?: 0} capitulos disponibles", style = WiText.small, modifier = Modifier.padding(top = 4.dp))
                WiButton(
                    "Escuchar desde el capitulo 1",
                    { queue.firstOrNull()?.let { onPlay(it, queue) } },
                    Modifier.fillMaxWidth().padding(top = 14.dp),
                    Icons.Rounded.Headphones,
                    color = WiCss.gold,
                )
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(books) { b ->
                    AssistChip(onClick = { selected = books.indexOf(b) }, label = { Text(b.name) })
                }
            }
        }
        items(queue) { track -> TrackCard(track) { onPlay(track, queue) } }
    }
}

@Composable
private fun MusicScreen(tracks: List<AudioTrack>, loading: Boolean, onRefresh: () -> Unit, onPlay: (AudioTrack, List<AudioTrack>) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            TopHero("Musica", "Adoracion, alabanza y reflexion", R.drawable.logo)
            Row(Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onRefresh) { Icon(Icons.Rounded.Refresh, contentDescription = "Actualizar") }
            }
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        if (!loading && tracks.isEmpty()) item { EmptyState("No hay canciones disponibles") }
        items(tracks) { track -> TrackCard(track) { onPlay(track, tracks) } }
    }
}

@Composable
private fun TrackCard(track: AudioTrack, onClick: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth(), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(track.artworkRes ?: R.drawable.logo),
                contentDescription = track.title,
                modifier = Modifier.size(54.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(track.title, style = WiText.h3, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(track.artist, style = WiText.small, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Rounded.PlayArrow, contentDescription = "Play", tint = WiCss.primary, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun QuotesScreen(publicQuotes: List<Quote>, privateQuotes: List<Quote>, loading: Boolean, viewModel: WiiHopeViewModel) {
    var privateMode by remember { mutableStateOf(false) }
    val quotes = if (privateMode) privateQuotes else publicQuotes
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            TopHero("Citas", if (privateMode) "Tus citas privadas" else "Inspiracion compartida", R.drawable.smile)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 10.dp)) {
                AssistChip(onClick = { privateMode = false }, label = { Text("Publicas (${publicQuotes.size})") })
                AssistChip(onClick = { privateMode = true }, label = { Text("Privadas (${privateQuotes.size})") }, leadingIcon = { Icon(Icons.Rounded.Lock, null) })
            }
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 8.dp))
        }
        if (!loading && quotes.isEmpty()) item { EmptyState(if (privateMode) "No tienes citas privadas" else "Aun no hay citas publicas") }
        items(quotes) { quote -> QuoteCard(quote, viewModel) }
    }
}

@Composable
private fun QuoteCard(quote: Quote, viewModel: WiiHopeViewModel) {
    var confirmDelete by remember { mutableStateOf(false) }
    GlassCard(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(quote.categoria, color = WiCss.primary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = { viewModel.toggleQuoteFavorite(quote) }) {
                Icon(if (quote.favorito) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, null, tint = if (quote.favorito) WiCss.error else WiCss.gray, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Rounded.Delete, null, tint = WiCss.error, modifier = Modifier.size(20.dp)) }
        }
        Text(quote.cita, style = WiText.body, modifier = Modifier.padding(vertical = 8.dp))
        Text("${quote.libro} · ${quote.nombreShow}", style = WiText.small)
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Eliminar cita") },
            text = { Text("Confirmas que quieres eliminar esta cita?") },
            confirmButton = { TextButton(onClick = { confirmDelete = false; viewModel.deleteQuote(quote) }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancelar") } },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuoteFab(viewModel: WiiHopeViewModel) {
    var open by remember { mutableStateOf(false) }
    FloatingActionButton(onClick = { open = true }, containerColor = WiCss.primary) {
        Icon(Icons.Rounded.Add, contentDescription = "Nueva cita", tint = WiCss.white, modifier = Modifier.size(22.dp))
    }
    if (open) {
        QuoteSheet(onDismiss = { open = false }, onSave = { quote ->
            viewModel.saveQuote(quote)
            open = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuoteSheet(onDismiss: () -> Unit, onSave: (Quote) -> Unit) {
    var cita by remember { mutableStateOf("") }
    var libro by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Fe") }
    var publico by remember { mutableStateOf(true) }
    var favorito by remember { mutableStateOf(false) }
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = WiCss.cream) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Nueva cita", style = WiText.h2)
            WiField(cita, { cita = it }, "Cita", singleLine = false, minLines = 4)
            WiField(libro, { libro = it }, "Referencia")
            WiField(categoria, { categoria = it }, "Categoria")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Publica", style = WiText.body)
                Switch(publico, { publico = it })
                Text("Favorita", style = WiText.body)
                Switch(favorito, { favorito = it })
            }
            WiButton("Guardar", { onSave(Quote(cita = cita.trim(), libro = libro.trim(), categoria = categoria.trim(), publico = publico, favorito = favorito)) }, Modifier.fillMaxWidth(), Icons.Rounded.Save)
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SettingsScreen(state: com.wiihope.app.feature.player.WiiHopeUiState, viewModel: WiiHopeViewModel) {
    val profile = state.profile
    var photo by remember(profile?.foto) { mutableStateOf(profile?.foto.orEmpty()) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { TopHero("Ajustes", "Perfil y preferencias", R.drawable.smile) }
        item {
            GlassCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(R.drawable.smile), contentDescription = "Perfil", modifier = Modifier.size(72.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                    Column(Modifier.padding(start = 14.dp).weight(1f)) {
                        Text("@${profile?.usuario ?: "usuario"}", style = WiText.h2)
                        Text(profile?.nombreCompleto.orEmpty(), style = WiText.body)
                        Text(profile?.email.orEmpty(), style = WiText.small)
                    }
                }
                WiField(photo, { photo = it }, "URL foto de perfil", modifier = Modifier.padding(top = 14.dp))
                WiButton("Actualizar foto", { viewModel.updatePhoto(photo) }, Modifier.fillMaxWidth().padding(top = 12.dp), Icons.Rounded.Save)
                WiButton("Cerrar sesion", viewModel::logout, Modifier.fillMaxWidth().padding(top = 10.dp), Icons.AutoMirrored.Rounded.Logout, color = WiCss.error)
                Text("${Wii.app} ${Wii.version} · ${Wii.by}", style = WiText.tiny, modifier = Modifier.padding(top = 14.dp))
            }
        }
    }
}

@Composable
private fun MiniPlayer(state: PlaybackState, onToggle: () -> Unit, onPrevious: () -> Unit, onNext: () -> Unit, onSeek: (Long) -> Unit) {
    val current = state.current ?: return
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = WiCss.white.copy(alpha = 0.64f)),
        border = WiCss.glassBorder(0.70f),
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(current.artworkRes ?: R.drawable.logo), contentDescription = current.title, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text(current.title, style = WiText.h3, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(current.artist, style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onPrevious) { Icon(Icons.Rounded.SkipPrevious, null, modifier = Modifier.size(24.dp)) }
                IconButton(onClick = onToggle, modifier = Modifier.clip(CircleShape).background(WiCss.primary)) {
                    Icon(if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = WiCss.white, modifier = Modifier.size(26.dp))
                }
                IconButton(onClick = onNext) { Icon(Icons.Rounded.SkipNext, null, modifier = Modifier.size(24.dp)) }
            }
            if (state.durationMs > 0) {
                Slider(
                    value = state.positionMs.toFloat().coerceIn(0f, state.durationMs.toFloat()),
                    valueRange = 0f..state.durationMs.toFloat(),
                    onValueChange = { onSeek(it.roundToLong()) },
                    modifier = Modifier.height(28.dp),
                )
            }
        }
    }
}
