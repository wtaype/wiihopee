package com.wiihope.app.feature.web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.wiihope.app.ui.components.GlassCard
import com.wiihope.app.feature.shell.WiPage
import com.wiihope.app.ui.components.WiButton
import com.wiihope.app.ui.components.WiHero
import com.wiihope.app.ui.theme.WiCss
import com.wiihope.app.ui.theme.WiText

private data class TermsSection(
    val title: String,
    val body: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val accent: androidx.compose.ui.graphics.Color,
)

@Composable
internal fun Terminos(onNavigate: (WiPage) -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            WiHero(
                "Terminos y Condiciones",
                "Uso claro, respetuoso y seguro de WiiHope para cuidar la experiencia de todos.",
                Icons.Rounded.Description,
            )
        }
        items(termsSections, key = { it.title }) { section ->
            LegalSectionCard(section)
        }
        item {
            WiButton(
                text = "Escribir al equipo",
                onClick = { uriHandler.openUri("mailto:wilder.taype@hotmail.com") },
                icon = Icons.Rounded.Email,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            WiButton(
                text = "Ver privacidad",
                onClick = { onNavigate(WiPage.Privacidad) },
                icon = Icons.Rounded.Lock,
                modifier = Modifier.fillMaxWidth(),
                outlined = true,
            )
        }
    }
}

@Composable
private fun LegalSectionCard(section: TermsSection) {
    GlassCard(modifier = Modifier.fillMaxWidth(), intensity = 0.52f) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(section.accent.copy(alpha = 0.14f), CircleShape),
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

private val termsSections = listOf(
    TermsSection(
        "Uso de la plataforma",
        "WiiHope es una plataforma digital gratuita dedicada a la meditacion, oracion y estudio biblico. Al usarla, te comprometes a cuidar el lenguaje, respetar la comunidad y no utilizar el espacio para spam, agresion o fines comerciales.",
        Icons.Rounded.Groups,
        WiCss.info,
    ),
    TermsSection(
        "Sincronizacion y datos",
        "Buscamos una experiencia hibrida y ligera: parte del contenido vive localmente para abrir rapido, y si inicias sesion sincronizamos lo esencial con Firebase para acompanarte en distintos dispositivos.",
        Icons.Rounded.Security,
        WiCss.primary,
    ),
    TermsSection(
        "Publicidad y servicios externos",
        "En la version web pueden existir anuncios y servicios externos para sostener el proyecto. En Android seguimos priorizando una experiencia limpia y honesta con lo que esta disponible hoy.",
        Icons.Rounded.Public,
        WiCss.secondary,
    ),
    TermsSection(
        "Derechos y propiedad intelectual",
        "WiiHope es un proyecto desarrollado por Wilder Taype. El diseno, codigo y funcionalidades pertenecen al proyecto, mientras que tu contenido personal sigue siendo tuyo.",
        Icons.Rounded.Gavel,
        WiCss.warning,
    ),
    TermsSection(
        "Limitacion de responsabilidad",
        "El contenido espiritual de WiiHope tiene fines de inspiracion y acompanamiento. No reemplaza atencion medica, psicologica, legal o profesional cuando sea necesaria.",
        Icons.Rounded.ReportProblem,
        WiCss.error,
    ),
    TermsSection(
        "Cambios en los terminos",
        "Podemos actualizar funciones, texto legal y detalles del servicio con el tiempo. El uso continuado de WiiHope despues de esos cambios implica aceptacion de la version vigente.",
        Icons.Rounded.Refresh,
        WiCss.info,
    ),
)
