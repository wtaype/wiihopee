package com.wiihope.app.feature.web

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wiihope.app.core.data.BlogRepository
import com.wiihope.app.core.model.BlogPost
import com.wiihope.app.core.model.Quote
import com.wiihope.app.core.model.UserProfile
import com.wiihope.app.feature.shell.WiPage
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.StatusPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.saludar
import com.wiihope.app.ui.components.wiDia
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

private data class HopeMessage(
    val title: String,
    val body: String,
    val reference: String,
)

private val fixedHopeMessages = listOf(
    HopeMessage("Confia hoy", "Dios sigue obrando incluso en los dias que parecen lentos.", "Romanos 8:28"),
    HopeMessage("Respira con calma", "No tienes que resolverlo todo ahora; puedes caminar paso a paso con fe.", "Salmos 46:10"),
    HopeMessage("No estas solo", "La gracia de Dios te acompana en lo sencillo y en lo dificil.", "Josue 1:9"),
    HopeMessage("Vuelve a empezar", "Cada manana trae misericordias nuevas para tu corazon.", "Lamentaciones 3:22-23"),
)

@Composable
internal fun Dashboard(
    profile: UserProfile?,
    quotes: List<Quote>,
    onNavigate: (WiPage) -> Unit,
) {
    val repository = remember { BlogRepository() }
    val blogs by produceState(initialValue = emptyList<BlogPost>()) {
        value = runCatching { repository.loadPage(limit = 4).posts }.getOrDefault(emptyList())
    }
    val phrases = remember(quotes) { buildDashboardMessages(quotes) }
    val featured = remember(phrases) { phrases.firstOrNull() ?: fixedHopeMessages.first() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                intensity = 0.82f,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    ProfileBadge(profile)
                    Column(modifier = Modifier.weight(1f)) {
                        GoldPill("Dashboard de fe")
                        Text(
                            text = "${saludar()} ${profile?.nombre?.ifBlank { profile.usuario } ?: "Smile"}",
                            style = WiText.h1,
                            modifier = Modifier.padding(top = 10.dp),
                        )
                        Text(
                            text = "Hoy es ${wiDia()}. Respira, vibra y avanza con paz.",
                            style = WiText.body,
                            modifier = Modifier.padding(top = 6.dp),
                        )
                    }
                }
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    intensity = 0.48f,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(WiCss.gold.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = WiCss.secondary)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(featured.title, style = WiText.h3)
                            Text(
                                featured.body,
                                style = WiText.body,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                            Text(
                                featured.reference,
                                style = WiText.small.copy(fontWeight = FontWeight.SemiBold, color = WiCss.primary),
                                modifier = Modifier.padding(top = 6.dp),
                            )
                        }
                    }
                }
            }
        }

        item {
            SectionHeader(
                title = "Mis frases positivas",
                subtitle = "Una mezcla de promesas guardadas y mensajes de Dios para hoy.",
                action = "Ver citas",
                onAction = { onNavigate(WiPage.Citas) },
            )
        }

        if (phrases.isEmpty()) {
            item {
                EmptyState(
                    message = "Todavia no hay frases listas. Entra a Citas y guarda tus favoritas.",
                    icon = Icons.Rounded.Bookmark,
                    actionText = "Ir a citas",
                    onAction = { onNavigate(WiPage.Citas) },
                )
            }
        } else {
            items(phrases.take(6), key = { it.reference + it.title }) { message ->
                PhraseCard(message)
            }
        }

        item {
            SectionHeader(
                title = "Ultimos blogs",
                subtitle = "Lecturas listas desde cache para abrir rapido y encontrar paz.",
                action = "Abrir blog",
                onAction = { onNavigate(WiPage.Blog) },
            )
        }

        if (blogs.isEmpty()) {
            item {
                EmptyState(
                    message = "No hay blogs recientes en cache todavia. Entra a Blog y en unos segundos quedaran listos.",
                    icon = Icons.Rounded.Newspaper,
                    actionText = "Ir a blog",
                    onAction = { onNavigate(WiPage.Blog) },
                )
            }
        } else {
            items(blogs, key = { it.routeId }) { post ->
                BlogPreviewCard(post = post, onClick = { onNavigate(WiPage.Blog) })
            }
        }
    }
}

@Composable
private fun ProfileBadge(profile: UserProfile?) {
    val photo = profile?.foto.orEmpty()
    if (photo.isNotBlank()) {
        AsyncImage(
            model = photo,
            contentDescription = profile?.nombreCompleto ?: "Perfil",
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(WiCss.white),
        )
    } else {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(WiCss.gold, WiCss.goldSoft),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initialsFor(profile),
                style = WiText.h3.copy(color = WiCss.black, fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String, action: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = WiText.h2)
            Text(subtitle, style = WiText.body, modifier = Modifier.padding(top = 4.dp))
        }
        WiButton(text = action, onClick = onAction, icon = Icons.Rounded.ArrowForward, modifier = Modifier.height(46.dp))
    }
}

@Composable
private fun PhraseCard(message: HopeMessage) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.54f) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(WiCss.gold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Favorite, contentDescription = null, tint = WiCss.secondary)
            }
            Column(Modifier.weight(1f)) {
                Text(message.body, style = WiText.h3.copy(fontWeight = FontWeight.Medium))
                Text(message.reference, style = WiText.small, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
private fun BlogPreviewCard(post: BlogPost, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        intensity = 0.56f,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = post.previewCover.ifBlank { post.cover },
                contentDescription = post.imagenAlt.ifBlank { post.titulo },
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(WiCss.bgSoft),
            )
            Column(Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    StatusPill(post.categoria.ifBlank { "Fe" }, WiCss.secondary)
                    if (post.pin) StatusPill("Fijado", WiCss.primary)
                }
                Text(
                    post.titulo,
                    style = WiText.h3,
                    modifier = Modifier.padding(top = 10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    post.resumen.ifBlank { "Reflexion premium lista para leer." },
                    style = WiText.body,
                    modifier = Modifier.padding(top = 6.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    post.tiempoLectura.ifBlank { "2 min" },
                    style = WiText.small.copy(color = WiCss.primary),
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

private fun buildDashboardMessages(quotes: List<Quote>): List<HopeMessage> {
    val quoteMessages = quotes
        .filter { it.cita.isNotBlank() }
        .take(4)
        .map {
            HopeMessage(
                title = it.categoria.ifBlank { "Promesa" },
                body = it.cita.trim(),
                reference = it.libro.ifBlank { "WiiHope" },
            )
        }

    return (quoteMessages + fixedHopeMessages)
        .distinctBy { it.body }
        .sortedBy { it.reference.length }
}

private fun initialsFor(profile: UserProfile?): String {
    val first = profile?.nombre?.firstOrNull()?.uppercaseChar()
        ?: profile?.usuario?.firstOrNull()?.uppercaseChar()
        ?: 'W'
    val last = profile?.apellidos?.firstOrNull()?.uppercaseChar()
    return listOfNotNull(first, last).joinToString("")
}
