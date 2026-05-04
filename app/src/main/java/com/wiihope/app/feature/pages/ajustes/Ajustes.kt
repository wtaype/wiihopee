package com.wiihope.app.feature.pages.ajustes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wiihope.app.R
import com.wiihope.app.core.app.Wii
import com.wiihope.app.core.model.UserProfile
import com.wiihope.app.feature.player.WiiHopeUiState
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.feature.web.Acerca
import com.wiihope.app.feature.web.Privacidad
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.WiGoldButton
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

private enum class AjustesPage { Main, Personal, Password, Privacidad, Acerca, Notificaciones }

@Composable
internal fun Ajustes(
    state: WiiHopeUiState,
    viewModel: WiiHopeViewModel,
    backSignal: Int = 0,
    onSubPageOpenChange: (Boolean) -> Unit = {},
) {
    var page by remember { mutableStateOf(AjustesPage.Main) }

    fun backToMain() {
        page = AjustesPage.Main
    }

    LaunchedEffect(page) {
        onSubPageOpenChange(page != AjustesPage.Main)
    }

    LaunchedEffect(backSignal) {
        if (backSignal > 0 && page != AjustesPage.Main) backToMain()
    }

    when (page) {
        AjustesPage.Main -> AjustesMain(
            profile = state.profile,
            onUpdatePhoto = viewModel::updatePhoto,
            onPersonal = { page = AjustesPage.Personal },
            onPassword = { page = AjustesPage.Password },
            onPrivacy = { page = AjustesPage.Privacidad },
            onAbout = { page = AjustesPage.Acerca },
            onNotifications = { page = AjustesPage.Notificaciones },
            onLogout = viewModel::logout,
        )
        AjustesPage.Personal -> SettingsSubPage(onBack = ::backToMain) { PersonalInfo(state.profile) }
        AjustesPage.Password -> SettingsSubPage(onBack = ::backToMain) { PasswordSettings(state.profile, viewModel::recover) }
        AjustesPage.Privacidad -> SettingsSubPage(onBack = ::backToMain) { Privacidad() }
        AjustesPage.Acerca -> SettingsSubPage(onBack = ::backToMain) { Acerca() }
        AjustesPage.Notificaciones -> SettingsSubPage(onBack = ::backToMain) { NotificationSettings() }
    }
}

@Composable
private fun AjustesMain(
    profile: UserProfile?,
    onUpdatePhoto: (String) -> Unit,
    onPersonal: () -> Unit,
    onPassword: () -> Unit,
    onPrivacy: () -> Unit,
    onAbout: () -> Unit,
    onNotifications: () -> Unit,
    onLogout: () -> Unit,
) {
    var photo by remember(profile?.foto) { mutableStateOf(profile?.foto.orEmpty()) }
    val avatar = profile?.foto.orEmpty().ifBlank { null }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.76f) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        AsyncImage(
                            model = avatar,
                            contentDescription = "Perfil",
                            modifier = Modifier.size(104.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.smile),
                            error = painterResource(R.drawable.smile),
                            fallback = painterResource(R.drawable.smile),
                        )
                        Box(Modifier.size(32.dp).clip(CircleShape).background(WiCss.gold), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Save, null, tint = WiCss.black, modifier = Modifier.size(16.dp))
                        }
                    }
                    Text(profile?.nombreCompleto?.ifBlank { profile.usuario } ?: "WiiHope", style = WiText.h2, modifier = Modifier.padding(top = 14.dp))
                    Text(profile?.email.orEmpty(), style = WiText.small, modifier = Modifier.padding(top = 2.dp))
                    GoldPill(profile?.rol?.ifBlank { "smile" } ?: "smile", modifier = Modifier.padding(top = 14.dp))
                    WiField(photo, { photo = it }, "URL foto de perfil", modifier = Modifier.padding(top = 18.dp))
                    WiGoldButton("Actualizar foto", { onUpdatePhoto(photo) }, Modifier.fillMaxWidth().padding(top = 12.dp), Icons.Rounded.Save)
                }
            }
        }
        item {
            Text("Ajustes de cuenta", style = WiText.label, color = WiCss.secondary, modifier = Modifier.padding(horizontal = 6.dp))
            GlassCard(Modifier.fillMaxWidth().padding(top = 8.dp), intensity = 0.58f) {
                Column {
                    SettingsRow(Icons.Rounded.Person, "Informacion personal", profile?.usuario?.let { "@$it" }.orEmpty(), onPersonal)
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Password, "Contrasena", "Recuperar acceso por correo", onPassword)
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Lock, "Privacidad", "Datos y seguridad", onPrivacy)
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Info, "Acerca de WiiHope", "${Wii.version} - ${Wii.by}", onAbout)
                    HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.22f))
                    SettingsRow(Icons.Rounded.Notifications, "Notificaciones", "Mensajes y recordatorios", onNotifications)
                }
            }
        }
        item {
            WiButton("Cerrar sesion", onLogout, Modifier.fillMaxWidth(), Icons.AutoMirrored.Rounded.Logout, color = WiCss.error, outlined = true)
            Text("${Wii.app} ${Wii.version}", style = WiText.tiny, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
        }
    }
}

@Composable
private fun PersonalInfo(profile: UserProfile?) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Informacion personal", "Datos de tu sesion actual.", Icons.Rounded.Person) }
        if (profile == null) {
            item { EmptyState("No hay informacion de perfil cargada") }
        } else {
            item {
                GlassCard(Modifier.fillMaxWidth(), intensity = 0.56f) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        InfoColumn("Email", profile.email)
                        InfoRow("Usuario", "@${profile.usuario}", "Rol", profile.rol)
                        InfoRow("Nombre", profile.nombre, "Apellidos", profile.apellidos)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(label.uppercase(), style = WiText.label.copy(color = WiCss.secondary))
        Text(
            value.ifBlank { "No registrado" },
            style = WiText.small.copy(color = WiCss.text500, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun InfoRow(leftLabel: String, leftValue: String, rightLabel: String, rightValue: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.weight(1f)) { InfoColumn(leftLabel, leftValue) }
        Box(Modifier.weight(1f)) { InfoColumn(rightLabel, rightValue) }
    }
}

@Composable
private fun PasswordSettings(profile: UserProfile?, onRecover: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Contrasena", "Recupera el acceso de forma segura.", Icons.Rounded.Password) }
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.56f) {
                Text("Correo de recuperacion", style = WiText.h3)
                Text(profile?.email.orEmpty().ifBlank { "No hay email cargado" }, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
                WiGoldButton(
                    text = "Enviar enlace de recuperacion",
                    onClick = { profile?.email?.takeIf { it.isNotBlank() }?.let(onRecover) },
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                    icon = Icons.Rounded.Lock,
                )
            }
        }
    }
}

@Composable
private fun NotificationSettings() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { WiHero("Notificaciones", "Mensajes y recordatorios importantes.", Icons.Rounded.Notifications) }
        item {
            GlassCard(Modifier.fillMaxWidth(), intensity = 0.56f) {
                Text("Estado", style = WiText.h3)
                Text(
                    "La base de notificaciones ya esta preparada. En el siguiente paso podemos separar mensajes, recordatorios y novedades.",
                    style = WiText.body,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun SettingsSubPage(onBack: () -> Unit, content: @Composable () -> Unit) {
    BackHandler(onBack = onBack)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                var totalDrag = 0f
                detectHorizontalDragGestures(
                    onDragStart = { totalDrag = 0f },
                    onHorizontalDrag = { _, dragAmount -> totalDrag += dragAmount },
                    onDragEnd = {
                        if (totalDrag > 120f) onBack()
                    },
                )
            },
    ) {
        content()
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
