package com.wiihope.app.feature.pages.musica

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
import androidx.compose.material.icons.rounded.RepeatOne
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.wiihope.app.R
import com.wiihope.app.core.app.Wii
import com.wiihope.app.core.audio.PlaybackState
import com.wiihope.app.core.model.AudioTrack
import com.wiihope.app.core.model.BibleBook
import com.wiihope.app.core.model.MusicLike
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.TrackSource
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.TrackArtwork
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.WiGoldButton
import com.wiihope.app.ui.components.saludar
import com.wiihope.app.ui.components.wiDia
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText
import kotlin.math.roundToLong

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun Musica(
    tracks: List<AudioTrack>,
    likes: List<MusicLike>,
    loading: Boolean,
    playback: PlaybackState,
    onRefresh: () -> Unit,
    onPlay: (AudioTrack, List<AudioTrack>) -> Unit,
    onToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onLoop: () -> Unit,
    onToggleLike: (AudioTrack) -> Unit,
) {
    val heroTrack = playback.current?.takeIf { it.source == TrackSource.Music } ?: tracks.firstOrNull()
    val likedIds = remember(likes) { likes.map { it.trackId }.toSet() }
    val favoriteTracks = remember(likes, tracks) {
        likes.mapNotNull { like ->
            tracks.firstOrNull { it.id == like.trackId } ?: like.toAudioTrack().takeIf { it.url.isNotBlank() }
        }
    }
    PullToRefreshBox(isRefreshing = loading, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
            item {
                MusicHero(
                    track = heroTrack,
                    isPlaying = playback.isPlaying && playback.current?.id == heroTrack?.id,
                    isLiked = heroTrack?.id?.let { likedIds.contains(it) } == true,
                    onPlay = {
                        when {
                            heroTrack == null -> Unit
                            playback.current?.id == heroTrack.id -> onToggle()
                            else -> onPlay(heroTrack, tracks.ifEmpty { listOf(heroTrack) })
                        }
                    },
                    onPrevious = onPrevious,
                    onNext = onNext,
                    onLoop = onLoop,
                    loopOne = playback.loopOne,
                    onLike = { heroTrack?.let(onToggleLike) },
                )
            }
            if (!loading && tracks.isEmpty()) item { EmptyState("No hay canciones disponibles") }
            if (tracks.isNotEmpty()) {
                item {
                    Text("Favoritos", style = WiText.h2)
                    if (favoriteTracks.isEmpty()) {
                        GlassCard(Modifier.fillMaxWidth().padding(top = 10.dp), intensity = 0.42f) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(38.dp).clip(CircleShape).background(WiCss.gold.copy(alpha = 0.22f)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Rounded.FavoriteBorder, null, tint = WiCss.secondary, modifier = Modifier.size(19.dp))
                                }
                                Text("Marca canciones favoritas para volver rapido.", style = WiText.small, modifier = Modifier.padding(start = 12.dp))
                            }
                        }
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(top = 12.dp)) {
                            items(favoriteTracks) { track ->
                                MusicAlbumCard(track = track, isPlaying = playback.current?.id == track.id && playback.isPlaying) {
                                    if (playback.current?.id == track.id) onToggle() else onPlay(track, favoriteTracks.ifEmpty { tracks })
                                }
                            }
                        }
                    }
                }
                item { Text("Lista de musica para ti", style = WiText.h2) }
                item {
                    GlassCard(Modifier.fillMaxWidth(), intensity = 0.62f) {
                        Column {
                            tracks.forEachIndexed { index, track ->
                                MusicTrackRow(
                                    track = track,
                                    isCurrent = playback.current?.id == track.id,
                                    isPlaying = playback.current?.id == track.id && playback.isPlaying,
                                    isLiked = likedIds.contains(track.id),
                                    onLike = { onToggleLike(track) },
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
}

@Composable
private fun MusicHero(
    track: AudioTrack?,
    isPlaying: Boolean,
    isLiked: Boolean,
    onPlay: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onLoop: () -> Unit,
    loopOne: Boolean,
    onLike: () -> Unit,
) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.78f) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                TrackArtwork(
                    track,
                    contentDescription = track?.title ?: "Musica",
                    modifier = Modifier.fillMaxWidth().height(230.dp).clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop,
                )
                IconButton(
                    onClick = onLike,
                    enabled = track != null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isLiked) WiCss.gold.copy(alpha = 0.92f) else WiCss.black.copy(alpha = 0.38f)),
                ) {
                    Icon(
                        if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        null,
                        tint = if (isLiked) WiCss.black else WiCss.white,
                        modifier = Modifier.size(23.dp),
                    )
                }
            }
            GoldPill(if (track == null) "WiiHope Music" else "Now Playing", modifier = Modifier.padding(top = 18.dp))
            Text(
                track?.title ?: "Musica",
                style = WiText.display.copy(fontSize = 29.sp, lineHeight = 35.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
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
                IconButton(
                    onClick = onLoop,
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(if (loopOne) WiCss.gold.copy(alpha = 0.42f) else Color.Transparent),
                ) {
                    Icon(Icons.Rounded.RepeatOne, null, tint = if (loopOne) WiCss.black else WiCss.primary, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun MusicAlbumCard(track: AudioTrack, isPlaying: Boolean, onClick: () -> Unit) {
    Column(Modifier.width(150.dp).clickable(onClick = onClick)) {
        Box {
            TrackArtwork(
                track,
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
private fun MusicTrackRow(
    track: AudioTrack,
    isCurrent: Boolean,
    isPlaying: Boolean,
    isLiked: Boolean,
    onLike: () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            TrackArtwork(
                track,
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
        IconButton(onClick = onLike, modifier = Modifier.size(38.dp)) {
            Icon(
                if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                null,
                tint = if (isLiked) WiCss.primary else WiCss.gray,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun TrackCard(track: AudioTrack, onClick: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth(), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TrackArtwork(
                track,
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

