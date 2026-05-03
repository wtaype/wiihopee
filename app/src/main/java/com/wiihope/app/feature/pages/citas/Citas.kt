package com.wiihope.app.feature.pages.citas

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
@OptIn(ExperimentalMaterial3Api::class)
internal fun Citas(publicQuotes: List<Quote>, privateQuotes: List<Quote>, loading: Boolean, viewModel: WiiHopeViewModel, onRefresh: () -> Unit) {
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
internal fun QuoteFab(viewModel: WiiHopeViewModel) {
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


