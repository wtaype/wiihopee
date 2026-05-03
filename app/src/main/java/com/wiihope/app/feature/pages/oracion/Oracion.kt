package com.wiihope.app.feature.pages.oracion

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
internal fun Oracion(
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

