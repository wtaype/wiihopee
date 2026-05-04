package com.wiihope.app.feature.pages.biblia

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.wiihope.app.core.model.BibleLike
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
internal fun Biblia(
    books: List<BibleBook>,
    likes: List<BibleLike>,
    playback: PlaybackState,
    onPlay: (AudioTrack, List<AudioTrack>) -> Unit,
    onToggle: () -> Unit,
    onToggleLike: (BibleBook, Int, AudioTrack) -> Unit,
) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var search by remember { mutableStateOf("") }
    val currentBibleTrackId = playback.current?.takeIf { it.source == TrackSource.Bible }?.id
    LaunchedEffect(currentBibleTrackId, books) {
        if (currentBibleTrackId != null) {
            books.indexOfFirst { currentBibleTrackId.startsWith("bible-${it.slug}-") }
                .takeIf { it >= 0 }
                ?.let { selected = it }
        }
    }
    val book = books.getOrNull(selected)
    val queue = book?.let { List(it.chapters) { chapter -> it.chapterTrack(chapter) } }.orEmpty()
    val filteredBooks = remember(search, books) {
        val clean = search.trim().lowercase()
        if (clean.isBlank()) books else books.filter { it.name.lowercase().contains(clean) }
    }
    val likedIds = remember(likes) { likes.map { it.id }.toSet() }
    val favoriteTracks = remember(likes, books) {
        likes.mapNotNull { like ->
            val favoriteBook = books.firstOrNull { it.slug == like.slug } ?: return@mapNotNull null
            val chapterIndex = (like.capitulo - 1).coerceIn(0, favoriteBook.chapters - 1)
            like to favoriteBook.chapterTrack(chapterIndex)
        }
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
            Column {
                Row(Modifier.fillMaxWidth().padding(horizontal = 2.dp), verticalAlignment = Alignment.Bottom) {
                    Text("Favoritos", style = WiText.h2, modifier = Modifier.weight(1f))
                    Text("${likes.size}", style = WiText.small)
                }
                if (favoriteTracks.isEmpty()) {
                    GlassCard(Modifier.fillMaxWidth().padding(top = 10.dp), intensity = 0.42f) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(38.dp).clip(CircleShape).background(WiCss.gold.copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(Icons.Rounded.FavoriteBorder, null, tint = WiCss.secondary, modifier = Modifier.size(19.dp))
                            }
                            Text(
                                "Guarda tus capitulos favoritos para volver rapido.",
                                style = WiText.small,
                                modifier = Modifier.padding(start = 12.dp),
                            )
                        }
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(top = 10.dp)) {
                        items(favoriteTracks) { (like, track) ->
                            val favoriteBook = books.first { it.slug == like.slug }
                            BibleFavoriteCard(
                                like = like,
                                isPlaying = playback.current?.id == track.id && playback.isPlaying,
                                onClick = {
                                    if (playback.current?.id == track.id) onToggle() else {
                                        val favoriteQueue = List(favoriteBook.chapters) { chapter -> favoriteBook.chapterTrack(chapter) }
                                        onPlay(track, favoriteQueue)
                                    }
                                },
                            )
                        }
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
        if (book != null) {
            item {
                GlassCard(Modifier.fillMaxWidth(), intensity = 0.62f) {
                    Column {
                        queue.forEachIndexed { index, track ->
                            val chapter = index + 1
                            BibleChapterRow(
                                book = book,
                                chapter = chapter,
                                track = track,
                                isCurrent = playback.current?.id == track.id,
                                isPlaying = playback.current?.id == track.id && playback.isPlaying,
                                isLiked = likedIds.any { it.endsWith("_${book.slug}_$chapter") },
                                onLike = { onToggleLike(book, chapter, track) },
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
}

@Composable
private fun BibleFavoriteCard(like: BibleLike, isPlaying: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WiCss.white.copy(alpha = 0.48f)),
        border = BorderStroke(1.dp, WiCss.goldSoft.copy(alpha = 0.58f)),
        modifier = Modifier.width(150.dp),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Favorite, null, tint = WiCss.secondary, modifier = Modifier.size(18.dp))
                Icon(if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = WiCss.primary, modifier = Modifier.size(20.dp))
            }
            Text("${like.libro} ${like.capitulo}", style = WiText.h3, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Nuevo Testamento", style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
    book: BibleBook,
    chapter: Int,
    track: AudioTrack,
    isCurrent: Boolean,
    isPlaying: Boolean,
    isLiked: Boolean,
    onLike: () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isCurrent) WiCss.white.copy(alpha = 0.38f) else Color.Transparent)
            .padding(horizontal = 0.dp, vertical = 12.dp),
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
        Column(Modifier.weight(1f).padding(start = 8.dp, end = 6.dp)) {
            Text("Capitulo $chapter", style = WiText.h3.copy(color = if (isCurrent) WiCss.primary else WiCss.text700))
            Text("${book.name} · Nuevo Testamento", style = WiText.small.copy(color = if (isCurrent) WiCss.secondary else WiCss.text300), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = onLike, modifier = Modifier.size(38.dp)) {
            Icon(
                if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                null,
                tint = if (isLiked) WiCss.secondary else WiCss.gray,
                modifier = Modifier.size(20.dp),
            )
        }
        Box(
            modifier = Modifier.size(38.dp).clip(CircleShape).background(if (isCurrent) WiCss.gold.copy(alpha = 0.28f) else WiCss.white.copy(alpha = 0.22f)),
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

