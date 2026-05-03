package com.wiihope.app.feature.shell

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
internal fun MiniPlayer(
    state: PlaybackState,
    onToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onLoop: () -> Unit,
    onSeek: (Long) -> Unit,
) {
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
                IconButton(
                    onClick = onLoop,
                    modifier = Modifier.clip(CircleShape).background(if (state.loopOne) WiCss.gold.copy(alpha = 0.40f) else Color.Transparent),
                ) {
                    Icon(Icons.Rounded.RepeatOne, null, tint = if (state.loopOne) WiCss.black else WiCss.text300, modifier = Modifier.size(21.dp))
                }
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

