package com.wiihope.app.feature.web

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wiihope.app.R
import com.wiihope.app.core.app.Wii
import com.wiihope.app.feature.shell.WiPage
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.ui.components.GoldPill
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.components.WiInfoCard
import com.wiihope.app.ui.components.WiSection
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

private const val SUPPORT_EMAIL = "mailto:wilder.taype@hotmail.com"
private const val ISSUES_URL = "https://github.com/wtaype/wiihope/issues"
private const val CHATWIL_WEB_URL = "https://wiihope.com/chatwil"

private data class MarketingCard(
    val title: String,
    val body: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val accent: androidx.compose.ui.graphics.Color,
)

@Composable
internal fun OraPorMi() = ChatWil()

@Composable
internal fun ChatWil() {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { HeroBanner("Ora por mi", "Acompanamiento espiritual via web por ahora.", "Disponible hoy en la version web mientras terminamos una experiencia movil segura.") }
        item {
            WiInfoCard(
                "Acompanamiento activo",
                "ChatWil y el espacio Ora por mi siguen vivos en WiiHope web. La version Android llegara cuando tengamos una capa segura para IA y mensajes.",
                Icons.Rounded.ChatBubble,
            )
        }
        item {
            WiButton(
                text = "Abrir en la web",
                onClick = { uriHandler.openUri(CHATWIL_WEB_URL) },
                icon = Icons.Rounded.OpenInNew,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
internal fun Acerca(onNavigate: (WiPage) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.jesus),
                contentDescription = "Acerca de WiiHope",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(248.dp)
                    .clip(RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        item {
            HeroBanner(
                badge = "Un rincon de paz en tu mundo digital",
                title = "WiiHope",
                body = "Encuentra inspiracion, sabiduria biblica y un espacio de oracion pensado para fortalecer tu espiritu cada dia. Respira, vibra y ama.",
            )
        }
        item {
            StatsBand(
                listOf(
                    "∞" to "Fe y esperanza",
                    "100%" to "Completamente gratis",
                    "24/7" to "Acompanamiento",
                    "2026" to "Siempre creciendo",
                ),
            )
        }
        item { WiSection("El proposito de WiiHope", "Una historia real, cercana y hecha con amor.") }
        item {
            GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.52f) {
                Text(
                    "WiiHope es una aplicacion disenada para compartir y aprender mas sobre la palabra de Dios. Su proposito es fortalecer la fe y convertirse en una herramienta de proteccion, prosperidad y bendicion para el dia a dia.",
                    style = WiText.body,
                )
                Text(
                    "Aqui vive el Padre Nuestro, al que tambien llamaban el telefono de Dios: una oracion sencilla, profunda y siempre disponible para volver a centrar el corazon.",
                    style = WiText.body,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    "Tambien reunimos frases de esperanza, un blog edificante y audios del Nuevo Testamento en quechua para que la experiencia sea cercana, ligera y verdaderamente util.",
                    style = WiText.body,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    "Con amor, Wilder Taype",
                    style = WiText.h3.copy(color = WiCss.primary),
                    modifier = Modifier.padding(top = 14.dp),
                )
            }
        }
        item { WiSection("Por que WiiHope", "Beneficios que buscan paz interior sin complicarte la vida.") }
        items(
            listOf(
                MarketingCard("Sabiduria en tu idioma", "Escucha y lee la Biblia con una experiencia simple, clara y rapida.", Icons.Rounded.AutoStories, WiCss.info),
                MarketingCard("Citas que inspiran", "Encuentra versiculos y promesas para compartir, guardar y volver a leer.", Icons.Rounded.Favorite, WiCss.secondary),
                MarketingCard("Blog edificante", "Reflexiones y articulos pensados para nutrir el alma y fortalecer la fe.", Icons.Rounded.Lightbulb, WiCss.primary),
                MarketingCard("Diseno inmersivo", "Cada detalle fue creado para sentirse suave, luminoso y sin distracciones.", Icons.Rounded.Spa, WiCss.warning),
            ),
        ) { card ->
            MarketingInfoCard(card)
        }
        item { WiSection("Como empezar", "Tres pasos sencillos para conectar.") }
        items(
            listOf(
                "1" to "Descubre citas y reflexiones para tu momento actual.",
                "2" to "Escucha la Biblia y la musica con un player persistente.",
                "3" to "Comparte una frase o un blog con alguien que necesite luz hoy.",
            ),
        ) { (step, text) ->
            StepCard(step, text)
        }
        item {
            DualActionRow(
                primaryText = "Leer la Biblia",
                primaryAction = { onNavigate(WiPage.Biblia) },
                secondaryText = "Ir al blog",
                secondaryAction = { onNavigate(WiPage.Blog) },
            )
        }
        item {
            WiInfoCard(
                "ChatWil y Ora por mi",
                "En movil preferimos ser honestos: ese acompanamiento sigue activo en la web y volvera a Android cuando la capa segura este lista.",
                Icons.Rounded.Web,
            )
        }
    }
}

@Composable
internal fun Privacidad(onNavigate: (WiPage) -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroBanner(
                badge = "Tu privacidad es sagrada",
                title = "Politica de Privacidad",
                body = "En WiiHope, tu privacidad no es solo una obligacion legal: es un valor central que guia como cuidamos tus datos.",
            )
        }
        item {
            StatsBand(
                listOf(
                    "0" to "Venta de datos",
                    "100%" to "Conexion cifrada",
                    "24/7" to "Proteccion y monitoreo",
                ),
            )
        }
        items(
            privacySections,
            key = { it.title },
        ) { section ->
            LegalCard(section)
        }
        item {
            DualActionRow(
                primaryText = "Escribir al equipo",
                primaryAction = { uriHandler.openUri(SUPPORT_EMAIL) },
                secondaryText = "Ver terminos",
                secondaryAction = { onNavigate(WiPage.Terminos) },
            )
        }
    }
}

@Composable
internal fun Feedback() {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroBanner(
                badge = "Tu opinion nos importa",
                title = "Feedback",
                body = "WiiHope crece gracias a tus ideas, tus reportes y lo que sientes al usar la app.",
            )
        }
        item { WiSection("Canales", "Elige la forma mas comoda de escribirnos.") }
        item {
            ActionCard(
                title = "GitHub Issues",
                body = "Reporta errores o pide nuevas funciones directamente en el repositorio.",
                icon = Icons.Rounded.Feedback,
                accent = WiCss.primary,
                cta = "Abrir GitHub",
                onClick = { uriHandler.openUri(ISSUES_URL) },
            )
        }
        item {
            ActionCard(
                title = "Correo directo",
                body = "Escribenos con tus comentarios, sugerencias o cualquier duda que tengas.",
                icon = Icons.Rounded.Email,
                accent = WiCss.info,
                cta = "Enviar correo",
                onClick = { uriHandler.openUri(SUPPORT_EMAIL) },
            )
        }
        item { WiSection("Categorias", "Asi puedes enfocar mejor tu mensaje.") }
        items(
            listOf(
                MarketingCard("Reportar un error", "Algo no funciona como deberia y quieres ayudarnos a corregirlo.", Icons.Rounded.Feedback, WiCss.error),
                MarketingCard("Sugerir una idea", "Tienes una mejora que haria WiiHope mas util o mas bonito.", Icons.Rounded.Lightbulb, WiCss.secondary),
                MarketingCard("Dejar una opinion", "Quieres contarnos como ha sido tu experiencia con la app.", Icons.Rounded.Star, WiCss.info),
                MarketingCard("Peticion espiritual", "Algo relacionado con contenido de fe, reflexion o acompanamiento.", Icons.Rounded.Spa, WiCss.primary),
            ),
        ) { card ->
            MarketingInfoCard(card)
        }
    }
}

@Composable
internal fun Contacto() {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroBanner(
                badge = "Estamos para ti",
                title = "Contacto",
                body = "Si tienes una duda, una sugerencia o solo quieres saludar, aqui tienes los caminos mas directos.",
            )
        }
        item { WiSection("Canales principales", "Compactos, claros y listos para usar.") }
        item {
            ActionCard(
                title = "Correo",
                body = "Escribenos a wilder.taype@hotmail.com y responderemos lo antes posible.",
                icon = Icons.Rounded.MailOutline,
                accent = WiCss.info,
                cta = "Abrir correo",
                onClick = { uriHandler.openUri(SUPPORT_EMAIL) },
            )
        }
        item {
            ActionCard(
                title = "GitHub Issues",
                body = "Ideal para mejoras, seguimiento tecnico y reportes detallados.",
                icon = Icons.Rounded.Public,
                accent = WiCss.primary,
                cta = "Abrir repositorio",
                onClick = { uriHandler.openUri(ISSUES_URL) },
            )
        }
        item { WiSection("Preguntas frecuentes", "Respuestas rapidas sobre soporte y uso.") }
        items(
            listOf(
                "Respondemos todos los mensajes" to "Si. Cada mensaje que recibimos se lee personalmente.",
                "WiiHope sigue siendo gratis" to "Si. Buscamos mantener la app accesible para todos.",
                "Tus mensajes son confidenciales" to "Solo los revisa el equipo de WiiHope; no se comparten con terceros.",
                "El formulario interno vendra despues" to "Por ahora preferimos correo y GitHub para mantener seguridad y simplicidad.",
            ),
        ) { (title, body) ->
            WiInfoCard(title, body, Icons.Rounded.ContactSupport)
        }
    }
}

@Composable
private fun HeroBanner(badge: String, title: String, body: String) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.82f) {
        GoldPill(badge)
        Text(text = title, style = WiText.display, modifier = Modifier.padding(top = 10.dp))
        Text(text = body, style = WiText.body, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun StatsBand(stats: List<Pair<String, String>>) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.56f) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            stats.chunked(2).forEach { rowStats ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    rowStats.forEach { (value, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(WiCss.white.copy(alpha = 0.34f))
                                .padding(horizontal = 14.dp, vertical = 16.dp),
                        ) {
                            Column {
                                Text(value, style = WiText.h2.copy(color = WiCss.primary))
                                Text(label, style = WiText.small, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MarketingInfoCard(card: MarketingCard) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.54f) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(card.accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(card.icon, contentDescription = null, tint = card.accent)
            }
            Column(Modifier.weight(1f)) {
                Text(card.title, style = WiText.h3)
                Text(card.body, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun StepCard(step: String, body: String) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.48f) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(WiCss.gold, WiCss.goldSoft))),
                contentAlignment = Alignment.Center,
            ) {
                Text(step, style = WiText.h3.copy(color = WiCss.black, fontWeight = FontWeight.Bold))
            }
            Text(body, style = WiText.body, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    body: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: androidx.compose.ui.graphics.Color,
    cta: String,
    onClick: () -> Unit,
) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.56f, onClick = onClick) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = accent)
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = WiText.h3)
                Text(body, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
            }
            StatusChip(cta, accent)
        }
    }
}

@Composable
private fun DualActionRow(
    primaryText: String,
    primaryAction: () -> Unit,
    secondaryText: String,
    secondaryAction: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        WiButton(
            text = primaryText,
            onClick = primaryAction,
            icon = Icons.Rounded.ArrowForward,
            modifier = Modifier.fillMaxWidth(),
        )
        WiButton(
            text = secondaryText,
            onClick = secondaryAction,
            icon = Icons.Rounded.OpenInNew,
            modifier = Modifier.fillMaxWidth(),
            outlined = true,
        )
    }
}

@Composable
private fun StatusChip(text: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text, style = WiText.tiny.copy(color = color, fontWeight = FontWeight.Bold))
        Icon(Icons.Rounded.ArrowForward, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
    }
}

private data class LegalSectionData(
    val title: String,
    val body: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val accent: androidx.compose.ui.graphics.Color,
)

@Composable
private fun LegalCard(section: LegalSectionData) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.52f) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(section.accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(section.icon, contentDescription = null, tint = section.accent)
            }
            Column(Modifier.weight(1f)) {
                Text(section.title, style = WiText.h3)
                Text(section.body, style = WiText.body, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

private val privacySections = listOf(
    LegalSectionData(
        "Informacion que recopilamos",
        "Solo pedimos lo necesario para brindarte una mejor experiencia espiritual: datos de cuenta, preferencias y contenido que decides guardar.",
        Icons.Rounded.Info,
        WiCss.info,
    ),
    LegalSectionData(
        "Como usamos tu informacion",
        "La usamos para personalizar la experiencia, sincronizar datos entre dispositivos y mejorar la estabilidad de WiiHope.",
        Icons.Rounded.Insights,
        WiCss.primary,
    ),
    LegalSectionData(
        "Publicidad y servicios externos",
        "En la web pueden existir servicios como Google AdSense. En movil seguimos priorizando una experiencia ligera y respetuosa.",
        Icons.Rounded.Public,
        WiCss.secondary,
    ),
    LegalSectionData(
        "Compartir informacion con terceros",
        "No vendemos ni alquilamos tus datos. Solo trabajamos con servicios clave como Firebase para autenticacion y almacenamiento.",
        Icons.Rounded.Groups,
        WiCss.warning,
    ),
    LegalSectionData(
        "Tus derechos",
        "Puedes pedir acceso, correccion o eliminacion de tu informacion escribiendo al equipo. Queremos que tengas control real sobre tus datos.",
        Icons.Rounded.Lock,
        WiCss.primary,
    ),
    LegalSectionData(
        "Seguridad",
        "Usamos conexiones cifradas y proveedores con estandares modernos de seguridad para cuidar tus sesiones y datos sincronizados.",
        Icons.Rounded.PrivacyTip,
        WiCss.info,
    ),
)
