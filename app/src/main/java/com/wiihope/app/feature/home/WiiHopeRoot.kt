package com.wiihope.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
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
import com.wiihope.app.core.model.TrackSource
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.WiGoldButton
import com.wiihope.app.ui.components.saludar
import com.wiihope.app.ui.components.wiDia
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText
import com.wiihope.app.ui.theme.premiumBackground
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

private enum class AuthMode { Login, Register, Recover }
private enum class MainTab(val label: String) { Oracion("Oracion"), Biblia("Biblia"), Citas("Citas"), Musica("Musica"), Ajustes("Ajustes") }
private enum class MenuPage { Menu, Blog, PrayerRequest, About, Discover, Messages, Notifications, Planning, NewPost, Notes, Terms, Privacy, Feedback, Contact }

private data class WiRouteItem(
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val tab: Int? = null,
    val page: MenuPage? = null,
)

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(74.dp).clip(CircleShape))
                    Column(Modifier.padding(start = 16.dp).weight(1f)) {
                        GoldPill("WiiHope")
                        Text("Fe, audio y esperanza", style = WiText.h1, modifier = Modifier.padding(top = 8.dp))
                        Text("Una experiencia espiritual simple, luminosa y personal.", style = WiText.body, modifier = Modifier.padding(top = 4.dp))
                    }
                }
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
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.58f) {
        Text("Iniciar sesion", style = WiText.h2)
        WiButton("Continuar con Google", onGoogleClick, Modifier.fillMaxWidth().padding(top = 14.dp), Icons.Rounded.AccountCircle, loading, color = WiCss.black)
        Text("o usa tu email", style = WiText.tiny, modifier = Modifier.fillMaxWidth().padding(top = 12.dp), textAlign = TextAlign.Center)
        WiField(user, { user = it.replace(" ", "").lowercase() }, "Email o usuario", leadingIcon = Icons.Rounded.Person, modifier = Modifier.padding(top = 14.dp))
        WiField(pass, { pass = it }, "Contrasena", leadingIcon = Icons.Rounded.Lock, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.padding(top = 10.dp))
        WiGoldButton("Entrar", { onLogin(user, pass) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.AutoMirrored.Rounded.Login, loading)
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
        WiGoldButton("Crear cuenta", { if (pass == pass2) onRegister(email, usuario, nombre, apellidos, grupo, genero, pass) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.Rounded.Person, loading)
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
        WiGoldButton(
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
        WiGoldButton("Enviar enlace", { onRecover(email) }, Modifier.fillMaxWidth().padding(top = 16.dp), Icons.Rounded.Mail, loading)
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainShell(
    state: com.wiihope.app.feature.player.WiiHopeUiState,
    playback: PlaybackState,
    launchScreen: String?,
    viewModel: WiiHopeViewModel,
    snackbar: SnackbarHostState,
) {
    val pagerState = rememberPagerState(pageCount = { MainTab.entries.size })
    val tab = pagerState.currentPage
    val scope = rememberCoroutineScope()
    var menuOpen by remember { mutableStateOf(false) }
    var menuPage by remember { mutableStateOf(MenuPage.Menu) }
    val openMenuPage: (MenuPage) -> Unit = {
        menuPage = it
        menuOpen = true
    }
    LaunchedEffect(launchScreen) {
        val target = when (launchScreen) {
            "bible" -> 1
            "quotes" -> 2
            "music", "player" -> 3
            "settings" -> 4
            else -> tab
        }
        if (target != pagerState.currentPage) pagerState.scrollToPage(target)
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            AppHeader(
                onHome = { scope.launch { pagerState.animateScrollToPage(0) } },
                onMessages = { openMenuPage(MenuPage.Messages) },
                onNotifications = { openMenuPage(MenuPage.Notifications) },
                onMenu = { openMenuPage(MenuPage.Menu) },
            )
        },
        floatingActionButton = {
            if (tab == 2) QuoteFab(viewModel)
        },
        bottomBar = {
            Column {
                if (tab != 0) {
                    MiniPlayer(playback, viewModel::togglePlayback, viewModel::previous, viewModel::next, viewModel::seekTo)
                }
                BottomNavGlass(tab) { index -> scope.launch { pagerState.animateScrollToPage(index) } }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).premiumBackground()) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                when (MainTab.entries[page]) {
                    MainTab.Oracion -> PrayerScreen(state.profile?.nombre.orEmpty(), playback, viewModel::play, viewModel::togglePlayback)
                    MainTab.Biblia -> BibleScreen(state.books, playback, viewModel::play, viewModel::togglePlayback)
                    MainTab.Citas -> QuotesScreen(state.publicQuotes, state.privateQuotes, state.quotesLoading, viewModel, onRefresh = { viewModel.refreshQuotes(forceServer = true) })
                    MainTab.Musica -> MusicScreen(
                        tracks = state.music,
                        loading = state.musicLoading,
                        playback = playback,
                        onRefresh = viewModel::refreshMusic,
                        onPlay = viewModel::play,
                        onToggle = viewModel::togglePlayback,
                        onPrevious = viewModel::previous,
                        onNext = viewModel::next,
                    )
                    MainTab.Ajustes -> SettingsScreenPremium(state, viewModel)
                }
            }
        }
    }
    if (menuOpen) {
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WiCss.black.copy(alpha = 0.18f))
                    .clickable { menuOpen = false },
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(336.dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp))
                    .background(WiCss.cream),
            ) {
            WiRoutesSheet(
                page = menuPage,
                role = state.profile?.rol.orEmpty(),
                onBack = { menuPage = MenuPage.Menu },
                onNavigateTab = { index ->
                    menuOpen = false
                    scope.launch { pagerState.animateScrollToPage(index) }
                },
                onOpenPage = { menuPage = it },
                onLogout = {
                    menuOpen = false
                    viewModel.logout()
                },
            )
            }
        }
    }
}

@Composable
private fun AppHeader(onHome: () -> Unit, onMessages: () -> Unit, onNotifications: () -> Unit, onMenu: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .statusBarsPadding()
            .height(54.dp)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            Wii.app,
            style = WiText.h3.copy(color = WiCss.primary),
            maxLines = 1,
            modifier = Modifier.weight(1f).clickable(onClick = onHome),
        )
        HeaderIcon(Icons.Rounded.Mail, "Mensajes", onMessages)
        HeaderIcon(Icons.Rounded.Notifications, "Notificaciones", onNotifications)
        HeaderIcon(Icons.Rounded.Menu, "Menu", onMenu)
    }
}

@Composable
private fun HeaderIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = label, tint = WiCss.primary, modifier = Modifier.size(21.dp))
    }
}

@Composable
private fun WiRoutesSheet(
    page: MenuPage,
    role: String,
    onBack: () -> Unit,
    onNavigateTab: (Int) -> Unit,
    onOpenPage: (MenuPage) -> Unit,
    onLogout: () -> Unit,
) {
    when (page) {
        MenuPage.Menu -> WiMenuHome(role, onNavigateTab, onOpenPage, onLogout)
        MenuPage.Blog -> WiMenuDetail("Super Blog", "Articulos, testimonios y reflexiones para alimentar la fe cada dia.", Icons.Rounded.Article, onBack) {
            MenuInfoCard("Proxima conexion", "Aqui conectaremos los posts del blog web/Firestore con lectura nativa, busqueda y favoritos.")
            MenuInfoCard("Categorias", "Devocionales, testimonios, familia, estudio biblico y vida diaria.")
        }
        MenuPage.PrayerRequest -> WiMenuDetail("Ora por mi", "Un espacio para escribir una peticion y recibir acompanamiento con esperanza.", Icons.Rounded.ChatBubble, onBack) {
            MenuInfoCard("Peticion de oracion", "Preparado para un formulario privado con motivo, mensaje y estado de seguimiento.")
            MenuInfoCard("Acompanamiento", "Luego podremos conectar respuestas, mensajes y notificaciones espirituales.")
        }
        MenuPage.About -> WiMenuDetail("Acerca de WiiHope", "Una app de fe creada con amor para orar, escuchar, leer y compartir esperanza.", Icons.Rounded.Info, onBack) {
            MenuInfoCard(Wii.app, "${Wii.desc}. Version ${Wii.version}.")
            MenuInfoCard("Creado por", "${Wii.by} · ${Wii.link}")
        }
        MenuPage.Discover -> WiMenuDetail("Descubre", "Explora todo lo que WiiHope puede hacer por tu vida espiritual.", Icons.Rounded.Explore, onBack) {
            MenuInfoCard("Audio Biblia", "Nuevo Testamento por libros y capitulos.")
            MenuInfoCard("Musica y oracion", "Player persistente, meditacion y contenido cristiano.")
            MenuInfoCard("Citas", "Promesas favoritas y notas privadas para guardar.")
        }
        MenuPage.Messages -> WiMenuDetail("Mensajes", "Centro de conversaciones y acompanamiento.", Icons.Rounded.Mail, onBack) {
            MenuInfoCard("Bandeja", "Pronto conectaremos mensajes de comunidad, respuestas de oracion y avisos personales.")
        }
        MenuPage.Notifications -> WiMenuDetail("Notificaciones", "Recordatorios y novedades importantes.", Icons.Rounded.Notifications, onBack) {
            MenuInfoCard("FCM listo", "La base ya puede recibir payloads para abrir Biblia, Citas, Musica o Ajustes.")
        }
        MenuPage.Planning -> WiMenuDetail("Planificar", "Herramientas para preparar contenido, devocionales y publicaciones.", Icons.Rounded.RocketLaunch, onBack) {
            MenuInfoCard("Roadmap", "Aqui creceran planes de lectura, borradores y agenda espiritual.")
        }
        MenuPage.NewPost -> WiMenuDetail("Nuevo Post", "Crear reflexiones y contenido para el blog.", Icons.Rounded.Add, onBack) {
            MenuInfoCard("Editor", "Reservado para editor nativo con titulo, categoria, portada y contenido.")
        }
        MenuPage.Notes -> WiMenuDetail("Notas", "Ideas, apuntes y pensamientos guardados.", Icons.Rounded.StickyNote2, onBack) {
            MenuInfoCard("Notas privadas", "Luego conectaremos una coleccion privada por usuario.")
        }
        MenuPage.Terms -> WiMenuDetail("Terminos", "Reglas simples para cuidar la comunidad.", Icons.Rounded.Description, onBack) {
            MenuInfoCard("Uso responsable", "WiiHope acompana espiritualmente, pero no reemplaza ayuda profesional cuando sea necesaria.")
        }
        MenuPage.Privacy -> WiMenuDetail("Privacidad", "Tu informacion debe estar protegida y clara.", Icons.Rounded.PrivacyTip, onBack) {
            MenuInfoCard("Datos", "Usamos Firebase Auth y Firestore para guardar perfil, citas, preferencias y tokens de notificacion.")
        }
        MenuPage.Feedback -> WiMenuDetail("Feedback", "Tus ideas ayudan a mejorar WiiHope.", Icons.Rounded.Feedback, onBack) {
            MenuInfoCard("Sugerencias", "Preparado para reportes, propuestas y comentarios desde la app.")
        }
        MenuPage.Contact -> WiMenuDetail("Contacto", "Canales para hablar con el equipo.", Icons.Rounded.ContactSupport, onBack) {
            MenuInfoCard("Soporte", "Pronto agregaremos email, redes y formulario directo.")
        }
    }
}

@Composable
private fun WiMenuHome(role: String, onNavigateTab: (Int) -> Unit, onOpenPage: (MenuPage) -> Unit, onLogout: () -> Unit) {
    val commonRoutes = listOf(
        WiRouteItem("Descubre", "Explora toda la experiencia WiiHope", Icons.Rounded.Explore, page = MenuPage.Discover),
        WiRouteItem("Super Blog", "Reflexiones y testimonios", Icons.Rounded.Article, page = MenuPage.Blog),
        WiRouteItem("Ora por mi", "Peticiones de oracion", Icons.Rounded.ChatBubble, page = MenuPage.PrayerRequest),
        WiRouteItem("Acerca", "Historia, mision y version", Icons.Rounded.Info, page = MenuPage.About),
    )
    val roleRoutes = when (role.lowercase()) {
        "admin" -> listOf(
            WiRouteItem("Plataforma", "Panel general", Icons.Rounded.Dashboard, page = MenuPage.Discover),
            WiRouteItem("Mensajes", "Centro de conversaciones", Icons.Rounded.Mail, page = MenuPage.Messages),
        )
        "gestor", "empresa" -> listOf(
            WiRouteItem("Dashboard", "Resumen de actividad", Icons.Rounded.Dashboard, page = MenuPage.Discover),
            WiRouteItem("Mensajes", "Centro de conversaciones", Icons.Rounded.Mail, page = MenuPage.Messages),
        )
        else -> listOf(
            WiRouteItem("Dashboard", "Tu espacio WiiHope", Icons.Rounded.Dashboard, page = MenuPage.Discover),
            WiRouteItem("Planificar", "Ideas y devocionales", Icons.Rounded.RocketLaunch, page = MenuPage.Planning),
            WiRouteItem("Nuevo Post", "Publicar reflexion", Icons.Rounded.Add, page = MenuPage.NewPost),
            WiRouteItem("Notas", "Apuntes privados", Icons.Rounded.StickyNote2, page = MenuPage.Notes),
            WiRouteItem("Mensajes", "Centro de conversaciones", Icons.Rounded.Mail, page = MenuPage.Messages),
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(48.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                Column(Modifier.padding(start = 12.dp).weight(1f)) {
                    Text("Menu", style = WiText.h2)
                    Text("Paginas extra y modulos nuevos", style = WiText.small)
                }
                GoldPill(role.ifBlank { "smile" })
            }
        }
        item { MenuSection("Nuevas paginas") }
        items(commonRoutes) { item -> WiRouteRow(item, onNavigateTab, onOpenPage) }
        item { MenuSection("Tu cuenta") }
        items(roleRoutes) { item -> WiRouteRow(item, onNavigateTab, onOpenPage) }
        item { MenuSection("Legal y contacto") }
        items(
            listOf(
                WiRouteItem("Terminos", "Condiciones de uso", Icons.Rounded.Description, page = MenuPage.Terms),
                WiRouteItem("Privacidad", "Como cuidamos tus datos", Icons.Rounded.PrivacyTip, page = MenuPage.Privacy),
                WiRouteItem("Feedback", "Ideas y mejoras", Icons.Rounded.Feedback, page = MenuPage.Feedback),
                WiRouteItem("Contacto", "Hablar con el equipo", Icons.Rounded.ContactSupport, page = MenuPage.Contact),
            )
        ) { item -> WiRouteRow(item, onNavigateTab, onOpenPage) }
        item {
            WiButton("Cerrar sesion", onLogout, Modifier.fillMaxWidth().padding(top = 4.dp), Icons.AutoMirrored.Rounded.Logout, color = WiCss.error, outlined = true)
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun MenuSection(title: String) {
    Text(title.uppercase(), style = WiText.label.copy(color = WiCss.secondary), modifier = Modifier.padding(top = 8.dp, start = 4.dp))
}

@Composable
private fun WiRouteRow(item: WiRouteItem, onNavigateTab: (Int) -> Unit, onOpenPage: (MenuPage) -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        intensity = 0.50f,
        onClick = {
            item.tab?.let(onNavigateTab)
            item.page?.let(onOpenPage)
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(WiCss.gold.copy(alpha = 0.20f)), contentAlignment = Alignment.Center) {
                Icon(item.icon, null, tint = WiCss.primary, modifier = Modifier.size(19.dp))
            }
            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(item.title, style = WiText.h3)
                Text(item.subtitle, style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Rounded.KeyboardArrowRight, null, tint = WiCss.gray, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun WiMenuDetail(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.clip(CircleShape).background(WiCss.white.copy(alpha = 0.62f))) {
                    Icon(Icons.Rounded.ArrowBack, null, tint = WiCss.primary, modifier = Modifier.size(20.dp))
                }
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(title, style = WiText.h2)
                    Text(subtitle, style = WiText.small)
                }
                Box(Modifier.size(46.dp).clip(CircleShape).background(WiCss.gold), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = WiCss.black, modifier = Modifier.size(21.dp))
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun MenuInfoCard(title: String, body: String) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.58f) {
        Text(title, style = WiText.h3)
        Text(body, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun BottomNavGlass(selectedTab: Int, onSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(WiCss.white)
            .padding(horizontal = 8.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val icons = listOf(Icons.Rounded.VolunteerActivism, Icons.Rounded.Book, Icons.Rounded.Favorite, Icons.Rounded.Headphones, Icons.Rounded.Settings)
        MainTab.entries.forEachIndexed { index, item ->
            BottomNavItemGlass(
                label = item.label,
                icon = icons[index],
                selected = selectedTab == index,
                onClick = { onSelected(index) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BottomNavItemGlass(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) WiCss.gold.copy(alpha = 0.20f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, contentDescription = label, tint = if (selected) WiCss.primary else WiCss.gray.copy(alpha = 0.72f), modifier = Modifier.size(18.dp))
        Text(label, style = WiText.tiny.copy(color = if (selected) WiCss.primary else WiCss.gray), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun TopHero(title: String, subtitle: String, imageRes: Int = R.drawable.jesus) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.80f) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(imageRes), contentDescription = title, modifier = Modifier.size(104.dp).clip(RoundedCornerShape(24.dp)), contentScale = ContentScale.Crop)
            Column(Modifier.padding(start = 16.dp).weight(1f)) {
                GoldPill("Luz diaria")
                Text(title, style = WiText.h2)
                Text(subtitle, style = WiText.body)
            }
        }
    }
}

@Composable
private fun PrayerScreen(
    name: String,
    playback: PlaybackState,
    onPlay: (AudioTrack, List<AudioTrack>) -> Unit,
    onToggle: () -> Unit,
) {
    val prayerTrack = remember {
        AudioTrack(
            id = "prayer-padre-nuestro-paz",
            title = "Padre Nuestro",
            artist = "WiiHope Oracion",
            subtitle = "Lectura suave",
            url = "https://github.com/geluksee/nice/raw/refs/heads/main/paz.mp3",
            source = TrackSource.Music,
            artworkRes = R.drawable.jesus,
        )
    }
    val isCurrentPrayer = playback.current?.id == prayerTrack.id
    val isPrayerPlaying = isCurrentPrayer && playback.isPlaying
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
            ) {
                Image(
                    painterResource(R.drawable.jesus),
                    contentDescription = "Jesus",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    WiCss.bgBase.copy(alpha = 0.38f),
                                    WiCss.bgBase,
                                ),
                            ),
                        ),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                ) {
                    Text("Devocion diaria", style = WiText.label)
                    Text(
                        "${saludar()}${if (name.isBlank()) "" else ",\n$name"}",
                        style = WiText.display,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                    Text(wiDia(), style = WiText.body, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                    Text("Oracion destacada", style = WiText.h2, modifier = Modifier.weight(1f))
                }
                GlassCard(Modifier.fillMaxWidth().padding(top = 12.dp), intensity = 0.72f) {
                    Box(Modifier.fillMaxWidth()) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(WiCss.gold),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(Icons.Rounded.VolunteerActivism, null, tint = WiCss.black, modifier = Modifier.size(22.dp))
                                }
                                Column(Modifier.padding(start = 12.dp)) {
                                    Text("Padre Nuestro", style = WiText.h2)
                                    Text("2 min · Oracion guiada", style = WiText.small)
                                }
                            }
                            Text(
                                "Padre nuestro, que estas en el cielo,\nsantificado sea tu Nombre;\nvenga a nosotros tu reino;\nhagase tu voluntad\nen la tierra como en el cielo.\n\nDanos hoy nuestro pan de cada dia;\nperdona nuestras ofensas,\ncomo tambien nosotros perdonamos\na los que nos ofenden;\nno nos dejes caer en la tentacion,\ny libranos del mal.",
                                style = WiText.body,
                                modifier = Modifier.padding(top = 18.dp),
                            )
                            WiGoldButton(
                                if (isPrayerPlaying) "Pausar oracion" else "Comenzar oracion",
                                onClick = {
                                    if (isCurrentPrayer) onToggle() else onPlay(prayerTrack, listOf(prayerTrack))
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                                icon = if (isPrayerPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerMiniCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    GlassCard(Modifier.width(162.dp).height(142.dp), intensity = 0.58f) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(WiCss.surfaceHigh),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, tint = WiCss.primary, modifier = Modifier.size(18.dp))
                }
                Icon(Icons.Rounded.FavoriteBorder, null, tint = WiCss.secondary.copy(alpha = 0.55f), modifier = Modifier.size(18.dp))
            }
            Column {
                Text(title, style = WiText.h3, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(subtitle, style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun PrayerRecommendationCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier.height(128.dp), intensity = 0.55f) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, null, tint = WiCss.primary, modifier = Modifier.size(24.dp))
            Column {
                Text(title, style = WiText.h3)
                Text(subtitle, style = WiText.tiny, modifier = Modifier.padding(top = 3.dp))
            }
        }
    }
}

@Composable
private fun BibleScreen(
    books: List<BibleBook>,
    playback: PlaybackState,
    onPlay: (AudioTrack, List<AudioTrack>) -> Unit,
    onToggle: () -> Unit,
) {
    var selected by remember { mutableIntStateOf(0) }
    var search by remember { mutableStateOf("") }
    val book = books.getOrNull(selected)
    val queue = book?.let { List(it.chapters) { chapter -> it.chapterTrack(chapter) } }.orEmpty()
    val filteredBooks = remember(search, books) {
        val clean = search.trim().lowercase()
        if (clean.isBlank()) books else books.filter { it.name.lowercase().contains(clean) }
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.78f) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(54.dp).clip(CircleShape).background(WiCss.gold),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Rounded.Book, null, tint = WiCss.black, modifier = Modifier.size(24.dp))
                    }
                    Column(Modifier.padding(start = 14.dp).weight(1f)) {
                        Text("Nuevo Testamento", style = WiText.h2)
                        Text("Biblia en audio por libros y capitulos", style = WiText.small)
                    }
                    Icon(Icons.Rounded.Headphones, null, tint = WiCss.secondary, modifier = Modifier.size(22.dp))
                }
                WiField(
                    value = search,
                    onValueChange = { search = it },
                    label = "Buscar libro o capitulo",
                    leadingIcon = Icons.Rounded.Search,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
        item {
            Column {
                Text("Libros", style = WiText.h2, modifier = Modifier.padding(horizontal = 2.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 10.dp)) {
                    items(filteredBooks) { b ->
                        val index = books.indexOf(b)
                        BibleBookChip(
                            title = b.name,
                            selected = selected == index,
                            onClick = { selected = index },
                        )
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 2.dp), verticalAlignment = Alignment.Bottom) {
                Text(book?.name ?: "Libro", style = WiText.h2, modifier = Modifier.weight(1f))
                Text("${book?.chapters ?: 0} capitulos", style = WiText.small)
            }
        }
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.62f) {
                Column {
                    queue.forEachIndexed { index, track ->
                        BibleChapterRow(
                            chapter = index + 1,
                            track = track,
                            isCurrent = playback.current?.id == track.id,
                            isPlaying = playback.current?.id == track.id && playback.isPlaying,
                            onClick = {
                                if (playback.current?.id == track.id) onToggle() else onPlay(track, queue)
                            },
                        )
                        if (index != queue.lastIndex) HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    }
                }
            }
        }
    }
}

@Composable
private fun BibleBookChip(title: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) WiCss.gold else WiCss.white.copy(alpha = 0.36f),
            contentColor = if (selected) WiCss.black else WiCss.text500,
        ),
        border = if (selected) null else BorderStroke(1.dp, WiCss.goldSoft.copy(alpha = 0.55f)),
    ) {
        Text(
            title,
            style = WiText.small.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            maxLines = 1,
        )
    }
}

@Composable
private fun BibleChapterRow(
    chapter: Int,
    track: AudioTrack,
    isCurrent: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isCurrent) WiCss.white.copy(alpha = 0.38f) else Color.Transparent)
            .padding(horizontal = 2.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(42.dp), contentAlignment = Alignment.Center) {
            if (isCurrent) {
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(4.dp).height(15.dp).clip(CircleShape).background(WiCss.secondary))
                    Box(Modifier.width(4.dp).height(23.dp).clip(CircleShape).background(WiCss.secondary))
                    Box(Modifier.width(4.dp).height(11.dp).clip(CircleShape).background(WiCss.secondary))
                }
            } else {
                Text(chapter.toString(), style = WiText.h3.copy(color = WiCss.gray))
            }
        }
        Column(Modifier.weight(1f).padding(start = 10.dp)) {
            Text("Capitulo $chapter", style = WiText.h3.copy(color = if (isCurrent) WiCss.primary else WiCss.text700))
            Text(track.subtitle, style = WiText.small.copy(color = if (isCurrent) WiCss.secondary else WiCss.text300))
        }
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isCurrent) WiCss.gold.copy(alpha = 0.28f) else WiCss.white.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                null,
                tint = if (isCurrent) WiCss.secondary else WiCss.gray,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun MusicScreen(
    tracks: List<AudioTrack>,
    loading: Boolean,
    playback: PlaybackState,
    onRefresh: () -> Unit,
    onPlay: (AudioTrack, List<AudioTrack>) -> Unit,
    onToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    val heroTrack = playback.current?.takeIf { it.source == TrackSource.Music } ?: tracks.firstOrNull()
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        item {
            MusicHero(
                track = heroTrack,
                isPlaying = playback.isPlaying && playback.current?.id == heroTrack?.id,
                onPlay = {
                    when {
                        heroTrack == null -> Unit
                        playback.current?.id == heroTrack.id -> onToggle()
                        else -> onPlay(heroTrack, tracks.ifEmpty { listOf(heroTrack) })
                    }
                },
                onPrevious = onPrevious,
                onNext = onNext,
            )
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 14.dp), color = WiCss.secondary)
        }
        if (!loading && tracks.isEmpty()) item { EmptyState("No hay canciones disponibles") }
        if (tracks.isNotEmpty()) {
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                    Text("Reproducidos reciente", style = WiText.h2, modifier = Modifier.weight(1f))
                    IconButton(onClick = onRefresh) { Icon(Icons.Rounded.Refresh, contentDescription = "Actualizar", tint = WiCss.primary, modifier = Modifier.size(20.dp)) }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(top = 12.dp)) {
                    items(tracks.take(8)) { track ->
                        MusicAlbumCard(track = track, isPlaying = playback.current?.id == track.id && playback.isPlaying) {
                            if (playback.current?.id == track.id) onToggle() else onPlay(track, tracks)
                        }
                    }
                }
            }
            item { Text("Curado para ti", style = WiText.h2) }
            item {
                GlassCard(Modifier.fillMaxWidth(), intensity = 0.62f) {
                    Column {
                        tracks.forEachIndexed { index, track ->
                            MusicTrackRow(
                                track = track,
                                isCurrent = playback.current?.id == track.id,
                                isPlaying = playback.current?.id == track.id && playback.isPlaying,
                                onClick = { if (playback.current?.id == track.id) onToggle() else onPlay(track, tracks) },
                            )
                            if (index != tracks.lastIndex) HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicHero(
    track: AudioTrack?,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.78f) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(track?.artworkRes ?: R.drawable.logo),
                contentDescription = track?.title ?: "Musica",
                modifier = Modifier.fillMaxWidth().height(230.dp).clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop,
            )
            GoldPill(if (track == null) "WiiHope Music" else "Now Playing", modifier = Modifier.padding(top = 18.dp))
            Text(track?.title ?: "Musica", style = WiText.display, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
            Text(track?.artist ?: "Adoracion, alabanza y reflexion", style = WiText.body, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 22.dp)) {
                IconButton(onClick = onPrevious) { Icon(Icons.Rounded.SkipPrevious, null, tint = WiCss.primary, modifier = Modifier.size(30.dp)) }
                IconButton(
                    onClick = onPlay,
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(WiCss.gold),
                ) {
                    Icon(if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = WiCss.black, modifier = Modifier.size(34.dp))
                }
                IconButton(onClick = onNext) { Icon(Icons.Rounded.SkipNext, null, tint = WiCss.primary, modifier = Modifier.size(30.dp)) }
            }
        }
    }
}

@Composable
private fun MusicAlbumCard(track: AudioTrack, isPlaying: Boolean, onClick: () -> Unit) {
    Column(Modifier.width(150.dp).clickable(onClick = onClick)) {
        Box {
            Image(
                painterResource(track.artworkRes ?: R.drawable.logo),
                contentDescription = track.title,
                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier.align(Alignment.Center).size(42.dp).clip(CircleShape).background(WiCss.black.copy(alpha = 0.34f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = WiCss.white, modifier = Modifier.size(25.dp))
            }
        }
        Text(track.title, style = WiText.h3, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 10.dp))
        Text(track.artist, style = WiText.small, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun MusicTrackRow(track: AudioTrack, isCurrent: Boolean, isPlaying: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            Image(
                painterResource(track.artworkRes ?: R.drawable.logo),
                contentDescription = track.title,
                modifier = Modifier.size(54.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            if (isCurrent) {
                Box(Modifier.matchParentSize().clip(RoundedCornerShape(12.dp)).background(WiCss.black.copy(alpha = 0.20f)), contentAlignment = Alignment.Center) {
                    Icon(if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = WiCss.white, modifier = Modifier.size(22.dp))
                }
            }
        }
        Column(Modifier.weight(1f).padding(horizontal = 13.dp)) {
            Text(track.title, style = WiText.h3.copy(color = if (isCurrent) WiCss.primary else WiCss.text700), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(track.artist, style = WiText.small, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Rounded.FavoriteBorder, null, tint = WiCss.gray, modifier = Modifier.size(20.dp))
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
@OptIn(ExperimentalMaterial3Api::class)
private fun QuotesScreen(publicQuotes: List<Quote>, privateQuotes: List<Quote>, loading: Boolean, viewModel: WiiHopeViewModel, onRefresh: () -> Unit) {
    var privateMode by remember { mutableStateOf(false) }
    val quotes = if (privateMode) privateQuotes else publicQuotes
    val featured = quotes.firstOrNull()
    val rest = if (featured == null) emptyList() else quotes.drop(1)
    PullToRefreshBox(isRefreshing = loading, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    GoldPill("Inspiracion diaria")
                    Text("Citas", style = WiText.display, color = WiCss.primary, modifier = Modifier.padding(top = 8.dp))
                    Text(
                        if (privateMode) "Tu coleccion privada de fe" else "Promesas y palabras para volver al centro",
                        style = WiText.body,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(26.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 22.dp)) {
                        QuoteTab("Publicas", publicQuotes.size, !privateMode) { privateMode = false }
                        QuoteTab("Privadas", privateQuotes.size, privateMode) { privateMode = true }
                    }
                    if (loading) LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 18.dp), color = WiCss.secondary)
                }
            }
            if (!loading && quotes.isEmpty()) item { EmptyState(if (privateMode) "No tienes citas privadas" else "Aun no hay citas publicas") }
            if (featured != null) item { FeaturedQuoteCard(featured, viewModel) }
            items(rest) { quote -> QuoteCard(quote, viewModel) }
        }
    }
}

@Composable
private fun QuoteTab(title: String, count: Int, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick).padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "$title ($count)",
            style = WiText.small.copy(color = if (selected) WiCss.primary else WiCss.text300, fontWeight = FontWeight.SemiBold),
        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(58.dp)
                .height(2.dp)
                .clip(CircleShape)
                .background(if (selected) WiCss.primary else Color.Transparent),
        )
    }
}

@Composable
private fun FeaturedQuoteCard(quote: Quote, viewModel: WiiHopeViewModel) {
    var confirmDelete by remember { mutableStateOf(false) }
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.76f) {
        Box(Modifier.fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GoldPill(quote.categoria.ifBlank { "Fe" })
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.toggleQuoteFavorite(quote) }) {
                        Icon(
                            if (quote.favorito) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            null,
                            tint = if (quote.favorito) WiCss.primary else WiCss.gray,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                    IconButton(onClick = { confirmDelete = true }) {
                        Icon(Icons.Rounded.Delete, null, tint = WiCss.error, modifier = Modifier.size(20.dp))
                    }
                }
                Text(
                    "\"${quote.cita}\"",
                    style = WiText.h2.copy(fontStyle = FontStyle.Italic),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp),
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Book, null, tint = WiCss.primary, modifier = Modifier.size(18.dp))
                    Text(quote.libro.ifBlank { quote.nombreShow.ifBlank { "WiiHope" } }, style = WiText.small, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
    if (confirmDelete) DeleteQuoteDialog(onDismiss = { confirmDelete = false }, onConfirm = { confirmDelete = false; viewModel.deleteQuote(quote) })
}

@Composable
private fun QuoteCard(quote: Quote, viewModel: WiiHopeViewModel) {
    var confirmDelete by remember { mutableStateOf(false) }
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.58f) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GoldPill(quote.categoria.ifBlank { "Fe" })
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { viewModel.toggleQuoteFavorite(quote) }) {
                Icon(if (quote.favorito) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, null, tint = if (quote.favorito) WiCss.primary else WiCss.gray, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Rounded.Delete, null, tint = WiCss.error, modifier = Modifier.size(20.dp)) }
        }
        Text(
            "\"${quote.cita}\"",
            style = WiText.body.copy(fontStyle = FontStyle.Italic),
            modifier = Modifier.padding(vertical = 16.dp),
        )
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

@Composable
private fun DeleteQuoteDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar cita") },
        text = { Text("Confirmas que quieres eliminar esta cita?") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Eliminar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuoteFab(viewModel: WiiHopeViewModel) {
    var open by remember { mutableStateOf(false) }
    FloatingActionButton(onClick = { open = true }, containerColor = WiCss.gold, contentColor = WiCss.black) {
        Icon(Icons.Rounded.Add, contentDescription = "Nueva cita", modifier = Modifier.size(24.dp))
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
            WiGoldButton("Guardar", { onSave(Quote(cita = cita.trim(), libro = libro.trim(), categoria = categoria.trim(), publico = publico, favorito = favorito)) }, Modifier.fillMaxWidth(), Icons.Rounded.Save)
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
private fun SettingsScreenPremium(state: com.wiihope.app.feature.player.WiiHopeUiState, viewModel: WiiHopeViewModel) {
    val profile = state.profile
    var photo by remember(profile?.foto) { mutableStateOf(profile?.foto.orEmpty()) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.76f) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Image(
                            painterResource(R.drawable.smile),
                            contentDescription = "Perfil",
                            modifier = Modifier.size(104.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                        Box(Modifier.size(32.dp).clip(CircleShape).background(WiCss.gold), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Save, null, tint = WiCss.black, modifier = Modifier.size(16.dp))
                        }
                    }
                    Text(profile?.nombreCompleto?.ifBlank { profile.usuario } ?: "WiiHope", style = WiText.h2, modifier = Modifier.padding(top = 14.dp))
                    Text(profile?.email.orEmpty(), style = WiText.small, modifier = Modifier.padding(top = 2.dp))
                    GoldPill(profile?.rol?.ifBlank { "smile" } ?: "smile", modifier = Modifier.padding(top = 14.dp))
                    WiField(photo, { photo = it }, "URL foto de perfil", modifier = Modifier.padding(top = 18.dp))
                    WiGoldButton("Actualizar foto", { viewModel.updatePhoto(photo) }, Modifier.fillMaxWidth().padding(top = 12.dp), Icons.Rounded.Save)
                }
            }
        }
        item {
            Text("Ajustes de cuenta", style = WiText.label, color = WiCss.secondary, modifier = Modifier.padding(horizontal = 6.dp))
            GlassCard(Modifier.fillMaxWidth().padding(top = 8.dp), intensity = 0.58f) {
                Column {
                    SettingsRow(Icons.Rounded.Person, "Informacion personal", profile?.usuario?.let { "@$it" }.orEmpty())
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Notifications, "Notificaciones", "Mensajes y recordatorios")
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Lock, "Privacidad y seguridad", "Cuenta protegida")
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Info, "Acerca de WiiHope", "${Wii.version} · ${Wii.by}")
                }
            }
        }
        item {
            WiButton("Cerrar sesion", viewModel::logout, Modifier.fillMaxWidth(), Icons.AutoMirrored.Rounded.Logout, color = WiCss.error, outlined = true)
            Text("${Wii.app} ${Wii.version}", style = WiText.tiny, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        }
    }
}

@Composable
private fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(WiCss.gold.copy(alpha = 0.16f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = WiCss.primary, modifier = Modifier.size(20.dp))
        }
        Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
            Text(title, style = WiText.h3)
            if (subtitle.isNotBlank()) Text(subtitle, style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Rounded.PlayArrow, null, tint = WiCss.gray, modifier = Modifier.size(18.dp))
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
