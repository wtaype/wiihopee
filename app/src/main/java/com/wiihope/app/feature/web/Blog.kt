package com.wiihope.app.feature.web

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.wiihope.app.R
import com.wiihope.app.core.data.BlogRepository
import com.wiihope.app.core.model.BlogPost
import com.wiihope.app.ui.components.EmptyState
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.StatusPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiField
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private const val FIRST_PAGE = 7L
private const val NEXT_PAGE = 3L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Blog() {
    val repository = remember { BlogRepository() }
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf(emptyList<BlogPost>()) }
    var cursor by remember { mutableStateOf<DocumentSnapshot?>(null) }
    var hasMore by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }
    var loadingMore by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Todo") }
    var selected by remember { mutableStateOf<BlogPost?>(null) }

    fun loadFirst(forceServer: Boolean = false) {
        scope.launch {
            loading = true
            error = null
            runCatching { repository.loadPage(FIRST_PAGE, forceServer = forceServer) }
                .onSuccess {
                    posts = it.posts
                    cursor = it.lastDoc
                    hasMore = it.hasMore
                }
                .onFailure { error = it.message ?: "No se pudo cargar el blog" }
            loading = false
        }
    }

    fun loadMore() {
        val nextCursor = cursor ?: return
        if (loadingMore || !hasMore) return
        scope.launch {
            loadingMore = true
            runCatching { repository.loadPage(NEXT_PAGE, startAfter = nextCursor, forceServer = true) }
                .onSuccess {
                    posts = (posts + it.posts).distinctBy { post -> post.routeId }
                    cursor = it.lastDoc ?: cursor
                    hasMore = it.hasMore
                }
                .onFailure { error = it.message ?: "No se pudo cargar mas" }
            loadingMore = false
        }
    }

    LaunchedEffect(Unit) { loadFirst() }

    selected?.let { post ->
        BlogPostDetail(
            preview = post,
            repository = repository,
            onBack = { selected = null },
        )
        return
    }

    val categories = remember(posts) {
        listOf("Todo") + posts.map { it.categoria }.filter { it.isNotBlank() }.distinct().sorted()
    }
    val filtered = remember(posts, query, category) {
        val clean = query.trim().lowercase()
        posts.filter { post ->
            (category == "Todo" || post.categoria.equals(category, ignoreCase = true)) &&
                (clean.isBlank() || listOf(post.titulo, post.resumen, post.categoria, post.autor).any { it.lowercase().contains(clean) })
        }
    }

    PullToRefreshBox(
        isRefreshing = loading,
        onRefresh = { loadFirst(forceServer = true) },
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                BlogHeader(total = posts.size)
            }
            item {
                WiField(
                    value = query,
                    onValueChange = { query = it },
                    label = "Buscar reflexiones",
                    leadingIcon = Icons.Rounded.Search,
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    items(categories) { item ->
                        AssistChip(
                            onClick = { category = item },
                            label = { Text(item, style = WiText.tiny.copy(fontWeight = FontWeight.Bold)) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (category == item) WiCss.gold else WiCss.white.copy(alpha = 0.42f),
                                labelColor = if (category == item) WiCss.black else WiCss.text700,
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = WiCss.goldSoft.copy(alpha = 0.65f),
                            ),
                        )
                    }
                }
            }
            if (error != null && posts.isEmpty()) {
                item { EmptyState(error ?: "No se pudo cargar el blog", actionText = "Reintentar", onAction = { loadFirst(forceServer = true) }) }
            } else if (!loading && filtered.isEmpty()) {
                item { EmptyState("No encontramos reflexiones con esa busqueda") }
            } else {
                items(filtered, key = { it.routeId }) { post ->
                    BlogCard(post = post, onClick = { selected = post })
                }
                if (hasMore && category == "Todo" && query.isBlank()) {
                    item {
                        WiButton(
                            text = if (loadingMore) "Cargando..." else "Cargar mas",
                            onClick = ::loadMore,
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            loading = loadingMore,
                            outlined = true,
                        )
                    }
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun BlogHeader(total: Int) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.76f) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(52.dp).clip(CircleShape).background(WiCss.gold), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Article, null, tint = WiCss.black, modifier = Modifier.size(24.dp))
                }
                Column(Modifier.weight(1f).padding(start = 14.dp)) {
                    GoldPill("Super Blog")
                    Text("Reflexiones para caminar con Dios", style = WiText.h2, modifier = Modifier.padding(top = 8.dp))
                    Text("$total articulos cargados en cache", style = WiText.small, modifier = Modifier.padding(top = 3.dp))
                }
            }
        }
    }
}

@Composable
private fun BlogCard(post: BlogPost, onClick: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth(), intensity = 0.62f, onClick = onClick) {
        Column {
            AsyncImage(
                model = post.previewCover.ifBlank { post.cover },
                contentDescription = post.imagenAlt.ifBlank { post.titulo },
                modifier = Modifier.fillMaxWidth().height(176.dp).clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Crop,
                placeholder = androidx.compose.ui.res.painterResource(R.drawable.jesus),
                error = androidx.compose.ui.res.painterResource(R.drawable.jesus),
                fallback = androidx.compose.ui.res.painterResource(R.drawable.jesus),
            )
            Row(Modifier.padding(top = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                StatusPill(post.categoria.ifBlank { "Fe" }, WiCss.secondary)
                if (post.pin) StatusPill("Fijado", WiCss.primary, Icons.Rounded.PushPin)
                if (post.tiempoLectura.isNotBlank()) StatusPill(post.tiempoLectura, WiCss.gray, Icons.Rounded.MenuBook)
            }
            Text(post.titulo, style = WiText.h2, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 12.dp))
            if (post.resumen.isNotBlank()) {
                Text(post.resumen, style = WiText.body, maxLines = 3, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 7.dp))
            }
            Row(Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("${post.autor} · ${formatDate(post.creado)}", style = WiText.tiny, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = WiCss.primary, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
private fun BlogPostDetail(preview: BlogPost, repository: BlogRepository, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var post by remember(preview.routeId) { mutableStateOf(preview) }
    var loading by remember(preview.routeId) { mutableStateOf(preview.contenidoMd.isBlank() && preview.contenido.isBlank()) }

    LaunchedEffect(preview.routeId) {
        loading = true
        runCatching { repository.loadPost(preview.routeId) }
            .onSuccess { if (it != null) post = it }
        loading = false
    }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Box {
                AsyncImage(
                    model = post.cover.ifBlank { post.previewCover },
                    contentDescription = post.imagenAlt.ifBlank { post.titulo },
                    modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = androidx.compose.ui.res.painterResource(R.drawable.jesus),
                    error = androidx.compose.ui.res.painterResource(R.drawable.jesus),
                    fallback = androidx.compose.ui.res.painterResource(R.drawable.jesus),
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(12.dp).size(44.dp).clip(CircleShape).background(WiCss.white.copy(alpha = 0.78f)),
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Volver", tint = WiCss.primary)
                }
            }
        }
        item {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    StatusPill(post.categoria.ifBlank { "Fe" }, WiCss.secondary)
                    if (post.tiempoLectura.isNotBlank()) StatusPill(post.tiempoLectura, WiCss.gray, Icons.Rounded.MenuBook)
                }
                Text(post.titulo, style = WiText.display.copy(fontSize = 32.sp, lineHeight = 38.sp), modifier = Modifier.padding(top = 12.dp))
                Text("${post.autor} · ${formatDate(post.creado)}", style = WiText.small, modifier = Modifier.padding(top = 8.dp))
            }
        }
        if (loading) {
            item {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = WiCss.primary)
                }
            }
        } else {
            items(parsePostBlocks(post)) { block ->
                BlogBlockView(block)
            }
            item {
                GlassCard(Modifier.fillMaxWidth(), intensity = 0.58f) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Favorite, null, tint = WiCss.primary, modifier = Modifier.size(20.dp))
                        Text("${post.likes} me gusta · ${post.vistas} vistas", style = WiText.small, modifier = Modifier.padding(start = 9.dp))
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

@Composable
private fun BlogBlockView(block: PostBlock) {
    when (block) {
        is PostBlock.Heading -> Text(block.text, style = WiText.h2.copy(color = WiCss.primary), modifier = Modifier.padding(top = 6.dp))
        is PostBlock.Paragraph -> Text(block.text, style = WiText.body.copy(fontSize = 17.sp, lineHeight = 27.sp), textAlign = TextAlign.Start)
        is PostBlock.Image -> AsyncImage(
            model = block.url,
            contentDescription = block.alt,
            modifier = Modifier.fillMaxWidth().height(230.dp).clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop,
            placeholder = androidx.compose.ui.res.painterResource(R.drawable.jesus),
            error = androidx.compose.ui.res.painterResource(R.drawable.jesus),
        )
        PostBlock.Divider -> HorizontalDivider(color = WiCss.goldSoft.copy(alpha = 0.48f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
    }
}

private sealed interface PostBlock {
    data class Heading(val text: String) : PostBlock
    data class Paragraph(val text: String) : PostBlock
    data class Image(val url: String, val alt: String) : PostBlock
    data object Divider : PostBlock
}

private fun parsePostBlocks(post: BlogPost): List<PostBlock> {
    val md = post.contenidoMd.trim()
    val source = if (md.isNotBlank()) md else htmlToMarkdownish(post.contenido)
    if (source.isBlank()) return listOf(PostBlock.Paragraph(post.resumen.ifBlank { "Reflexion disponible muy pronto." }))

    val imageRegex = Regex("!\\[([^]]*)]\\(([^)]+)\\)")
    val normalized = source
        .replace(imageRegex) { "\n${it.value}\n" }
        .replace(Regex("\\s+---\\s+"), "\n---\n")
        .replace(Regex("\\s+(#{1,3}\\s+)"), "\n$1")

    return normalized.lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .mapNotNull { line ->
            when {
                line == "---" -> PostBlock.Divider
                imageRegex.matches(line) -> {
                    val match = imageRegex.matchEntire(line) ?: return@mapNotNull null
                    PostBlock.Image(match.groupValues[2], match.groupValues[1])
                }
                line.startsWith("#") -> PostBlock.Heading(line.replace(Regex("^#+\\s*"), "").cleanInlineMarkdown())
                else -> PostBlock.Paragraph(line.cleanInlineMarkdown())
            }
        }
}

private fun htmlToMarkdownish(html: String): String {
    if (html.isBlank()) return ""
    return html
        .replace(Regex("<img[^>]*alt=[\"']([^\"']*)[\"'][^>]*src=[\"']([^\"']+)[\"'][^>]*>", RegexOption.IGNORE_CASE), "\n![$1]($2)\n")
        .replace(Regex("<img[^>]*src=[\"']([^\"']+)[\"'][^>]*alt=[\"']([^\"']*)[\"'][^>]*>", RegexOption.IGNORE_CASE), "\n![$2]($1)\n")
        .replace(Regex("</h[1-3]>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("<h[1-3][^>]*>", RegexOption.IGNORE_CASE), "\n## ")
        .replace(Regex("</p>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("<hr[^>]*>", RegexOption.IGNORE_CASE), "\n---\n")
        .replace(Regex("<[^>]+>"), "")
        .replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
}

private fun String.cleanInlineMarkdown(): String =
    replace(Regex("\\*\\*(.*?)\\*\\*"), "$1")
        .replace(Regex("\\*(.*?)\\*"), "$1")
        .trim()

private fun formatDate(timestamp: Timestamp?): String {
    val date = timestamp?.toDate() ?: return "Reciente"
    return SimpleDateFormat("d MMM yyyy", Locale("es", "PE")).format(date)
}
