package com.wiihope.app.feature.pages.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.UserProfile
import com.wiihope.app.feature.player.WiiHopeViewModel
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.components.WiGoldButton
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun Citas(
    publicQuotes: List<Quote>,
    total: Long,
    hasMore: Boolean,
    loading: Boolean,
    loadingMore: Boolean,
    currentUser: UserProfile?,
    viewModel: WiiHopeViewModel,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    var search by remember { mutableStateOf("") }
    val filtered = remember(publicQuotes, search) {
        val clean = search.trim().lowercase()
        if (clean.isBlank()) publicQuotes else publicQuotes.filter {
            it.cita.lowercase().contains(clean) ||
                it.libro.lowercase().contains(clean) ||
                it.nombreShow.lowercase().contains(clean) ||
                it.categoria.lowercase().contains(clean)
        }
    }
    val featured = filtered.firstOrNull()
    val rest = featured?.let { filtered.drop(1) }.orEmpty()

    PullToRefreshBox(isRefreshing = loading, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                QuotesHeader(
                    total = total,
                    loaded = publicQuotes.size,
                    search = search,
                    onSearch = { search = it },
                    loading = loading,
                )
            }
            if (!loading && filtered.isEmpty()) {
                item { EmptyState(if (search.isBlank()) "Aun no hay citas publicas" else "No encontramos citas cargadas") }
            }
            if (featured != null) {
                item { FeaturedQuoteCard(featured, currentUser, viewModel) }
            }
            items(rest, key = { it.id }) { quote ->
                QuoteCard(quote, currentUser, viewModel)
            }
            if (hasMore) {
                item {
                    WiGoldButton(
                        text = if (loadingMore) "Cargando" else "Cargar mas",
                        onClick = onLoadMore,
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Rounded.Refresh,
                        loading = loadingMore,
                    )
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun QuotesHeader(
    total: Long,
    loaded: Int,
    search: String,
    onSearch: (String) -> Unit,
    loading: Boolean,
) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.78f) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(54.dp).clip(CircleShape).background(WiCss.gold),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.FormatQuote, null, tint = WiCss.black, modifier = Modifier.size(25.dp))
            }
            Column(Modifier.padding(start = 14.dp).weight(1f)) {
                Text("Inspiracion diaria", style = WiText.label)
                Text("Citas", style = WiText.h2, modifier = Modifier.padding(top = 3.dp))
                Text("Promesas y palabras para volver al centro", style = WiText.small)
            }
        }
        WiField(
            value = search,
            onValueChange = onSearch,
            label = "Buscar citas para el alma (${total.takeIf { it > 0 } ?: loaded})",
            leadingIcon = Icons.Rounded.Search,
            modifier = Modifier.padding(top = 16.dp),
        )
        if (loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 14.dp), color = WiCss.secondary)
        }
    }
}

@Composable
private fun FeaturedQuoteCard(quote: Quote, currentUser: UserProfile?, viewModel: WiiHopeViewModel) {
    QuoteCardShell(quote = quote, featured = true, currentUser = currentUser, viewModel = viewModel) {
        Text(
            "\"${quote.cita}\"",
            style = WiText.body.copy(fontSize = 17.sp, lineHeight = 25.sp, color = WiCss.text700),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        )
    }
}

@Composable
private fun QuoteCard(quote: Quote, currentUser: UserProfile?, viewModel: WiiHopeViewModel) {
    QuoteCardShell(quote = quote, featured = false, currentUser = currentUser, viewModel = viewModel) {
        Text(
            "\"${quote.cita}\"",
            style = WiText.body.copy(fontSize = 17.sp, lineHeight = 25.sp, color = WiCss.text700),
            modifier = Modifier.padding(vertical = 15.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun QuoteCardShell(
    quote: Quote,
    featured: Boolean,
    currentUser: UserProfile?,
    viewModel: WiiHopeViewModel,
    content: @Composable () -> Unit,
) {
    var confirmDelete by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf(false) }
    var actionsOpen by remember(quote.id) { mutableStateOf(false) }
    val canManage = quote.isMine(currentUser)
    GlassCard(
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (canManage) actionsOpen = !actionsOpen },
                onLongClick = { if (canManage) actionsOpen = !actionsOpen },
            ),
        intensity = if (featured) 0.76f else 0.58f,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GoldPill(quote.categoria.ifBlank { "Fe" })
            Spacer(Modifier.weight(1f))
            if (canManage && actionsOpen) {
                IconButton(onClick = { editing = true }, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Rounded.Edit, null, tint = WiCss.text300, modifier = Modifier.size(19.dp))
                }
                IconButton(onClick = { confirmDelete = true }, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Rounded.Delete, null, tint = WiCss.text300, modifier = Modifier.size(19.dp))
                }
            }
            IconButton(onClick = { viewModel.toggleQuoteFavorite(quote) }, modifier = Modifier.size(38.dp)) {
                Icon(
                    if (quote.favorito) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    null,
                    tint = if (quote.favorito) WiCss.primary else WiCss.gray,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        content()
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            Text(
                quoteFooter(quote),
                style = WiText.small,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
    if (confirmDelete) DeleteQuoteDialog(onDismiss = { confirmDelete = false }, onConfirm = { confirmDelete = false; viewModel.deleteQuote(quote) })
    if (editing) {
        QuoteSheet(
            initial = quote,
            onDismiss = { editing = false },
            onSave = { edited ->
                viewModel.saveQuote(edited)
                editing = false
            },
        )
    }
}

private fun quoteFooter(quote: Quote): String {
    val libro = quote.libro.ifBlank { "WiiHope" }
    val autor = quote.nombreShow.ifBlank { quote.usuario.ifBlank { "Comunidad" } }.smartCapitalize()
    return "$libro · $autor"
}

private fun Quote.isMine(profile: UserProfile?): Boolean {
    if (profile == null) return false
    return usuario.equals(profile.usuario, ignoreCase = true) ||
        email.equals(profile.email, ignoreCase = true)
}

private fun String.smartCapitalize(): String =
    trim()
        .lowercase()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

@Composable
private fun DeleteQuoteDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar cita") },
        text = { Text("Esta accion no se puede deshacer. Confirmas que quieres eliminar esta cita?") },
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
        QuoteSheet(initial = null, onDismiss = { open = false }, onSave = { quote ->
            viewModel.saveQuote(quote)
            open = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuoteSheet(initial: Quote?, onDismiss: () -> Unit, onSave: (Quote) -> Unit) {
    var cita by remember(initial?.id) { mutableStateOf(initial?.cita.orEmpty()) }
    var libro by remember(initial?.id) { mutableStateOf(initial?.libro.orEmpty()) }
    var categoria by remember(initial?.id) { mutableStateOf(initial?.categoria ?: "Perdon") }
    var favorito by remember(initial?.id) { mutableStateOf(initial?.favorito ?: false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = WiCss.cream) {
        Column(
            Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(if (initial == null) "Nueva cita" else "Editar cita", style = WiText.h2)
            WiField(cita, { cita = it }, "Cita", singleLine = false, minLines = 3)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                WiField(libro, { libro = it }, "Referencia", modifier = Modifier.weight(1f))
                WiField(categoria, { categoria = it }, "Categoria", modifier = Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Favorita", style = WiText.body)
                Checkbox(favorito, { favorito = it })
            }
            WiGoldButton(
                "Guardar",
                {
                    onSave(
                        (initial ?: Quote()).copy(
                            cita = cita.trim(),
                            libro = libro.trim(),
                            categoria = categoria.trim(),
                            publico = true,
                            favorito = favorito,
                        )
                    )
                },
                Modifier.fillMaxWidth(),
                Icons.Rounded.Save,
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}
