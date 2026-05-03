package com.wiihope.app.feature.pages.ajustes

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
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChatBubble
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
import androidx.compose.material.icons.rounded.KeyboardArrowRight
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
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.StickyNote2
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
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
import kotlin.math.roundToLong

@Composable
internal fun Ajustes(state: com.wiihope.app.feature.player.WiiHopeUiState, viewModel: WiiHopeViewModel) {
    val profile = state.profile
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

