package com.wiihope.app.feature.shell

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
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
internal fun MiniPlayer(
    state: PlaybackState,
    onOpen: (TrackSource) -> Unit,
    onToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onLoop: () -> Unit,
    onSeek: (Long) -> Unit,
) {
    val current = state.current ?: return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WiCss.bgBase.copy(alpha = 0.97f)),
    ) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(WiCss.goldSoft.copy(alpha = 0.42f)))
        Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier.weight(1f).clickable { onOpen(current.source) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TrackArtwork(
                        current,
                        contentDescription = current.title,
                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Column(Modifier.weight(1f).padding(start = 10.dp, end = 8.dp)) {
                        Text(current.title, style = WiText.h3, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(current.artist, style = WiText.tiny, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    CompactControl(onPrevious) { Icon(Icons.Rounded.SkipPrevious, null, tint = WiCss.text700, modifier = Modifier.size(22.dp)) }
                    CompactControl(onToggle, modifier = Modifier.size(38.dp).clip(CircleShape).background(WiCss.primary)) {
                        Icon(if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = WiCss.white, modifier = Modifier.size(24.dp))
                    }
                    CompactControl(onNext) { Icon(Icons.Rounded.SkipNext, null, tint = WiCss.text700, modifier = Modifier.size(22.dp)) }
                    CompactControl(onLoop, modifier = Modifier.size(34.dp).clip(CircleShape).background(if (state.loopOne) WiCss.gold.copy(alpha = 0.44f) else Color.Transparent)) {
                        Icon(Icons.Rounded.RepeatOne, null, tint = if (state.loopOne) WiCss.black else WiCss.text300, modifier = Modifier.size(19.dp))
                    }
                }
            }
            if (state.durationMs > 0) {
                MiniSeekBar(
                    positionMs = state.positionMs,
                    durationMs = state.durationMs,
                    onSeek = onSeek,
                    modifier = Modifier.padding(top = 6.dp),
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(formatTime(state.positionMs), style = WiText.tiny)
                    Text(formatTime(state.durationMs), style = WiText.tiny)
                }
            }
        }
    }
}

@Composable
private fun MiniSeekBar(
    positionMs: Long,
    durationMs: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var widthPx by remember { mutableIntStateOf(0) }
    val progress = (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)

    fun seekTo(x: Float) {
        if (widthPx <= 0) return
        val fraction = (x / widthPx).coerceIn(0f, 1f)
        onSeek((durationMs * fraction).roundToLong())
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .onSizeChanged { widthPx = it.width }
            .pointerInput(durationMs) {
                detectDragGestures(
                    onDragStart = { seekTo(it.x) },
                    onDrag = { change, _ ->
                        seekTo(change.position.x)
                        change.consume()
                    },
                )
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(CircleShape)
                .background(WiCss.goldSoft.copy(alpha = 0.28f)),
        )
        Box(
            Modifier
                .fillMaxWidth(progress)
                .height(3.dp)
                .clip(CircleShape)
                .background(WiCss.secondary),
        )
    }
}

@Composable
private fun CompactControl(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.size(34.dp).clip(CircleShape),
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        content()
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0L)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}

