package com.wiihope.app.feature.shell

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector

data class WiRoute(
    val page: WiPage,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
)

object WiRoutes {
    fun roleRoutes(role: String): List<WiRoute> = when (role.lowercase()) {
        "admin" -> listOf(
            WiRoute(WiPage.Musica, "Musica", "Alabanza y player premium", Icons.Rounded.Headphones),
            WiRoute(WiPage.Notas, "Notas", "Tarjetas privadas locales", Icons.Rounded.StickyNote2),
            WiRoute(WiPage.Planificar, "Planificar", "Documentos locales", Icons.Rounded.RocketLaunch),
            WiRoute(WiPage.Dashboard, "Dashboard", "Tu espacio WiiHope", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Admin, "Admin", "Panel general", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Usuarios, "Usuarios", "Gestion de comunidad", Icons.Rounded.Dashboard),
        )
        "gestor", "empresa" -> listOf(
            WiRoute(WiPage.Musica, "Musica", "Alabanza y player premium", Icons.Rounded.Headphones),
            WiRoute(WiPage.Notas, "Notas", "Tarjetas privadas locales", Icons.Rounded.StickyNote2),
            WiRoute(WiPage.Planificar, "Planificar", "Documentos locales", Icons.Rounded.RocketLaunch),
            WiRoute(WiPage.Dashboard, "Dashboard", "Tu espacio WiiHope", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Gestor, "Gestor", "Resumen de actividad", Icons.Rounded.Dashboard),
        )
        else -> listOf(
            WiRoute(WiPage.Musica, "Musica", "Alabanza y player premium", Icons.Rounded.Headphones),
            WiRoute(WiPage.Notas, "Notas", "Tarjetas privadas locales", Icons.Rounded.StickyNote2),
            WiRoute(WiPage.Planificar, "Planificar", "Documentos locales", Icons.Rounded.RocketLaunch),
            WiRoute(WiPage.Dashboard, "Dashboard", "Tu espacio WiiHope", Icons.Rounded.Dashboard),
        )
    }

    val about = listOf(
        WiRoute(WiPage.Acerca, "Acerca", "Historia, mision y version", Icons.Rounded.Info),
        WiRoute(WiPage.Descubre, "Descubre", "Explora toda la experiencia", Icons.Rounded.Explore),
        WiRoute(WiPage.Terminos, "Terminos", "Condiciones de uso", Icons.Rounded.Description),
        WiRoute(WiPage.Privacidad, "Privacidad", "Como cuidamos tus datos", Icons.Rounded.PrivacyTip),
        WiRoute(WiPage.Feedback, "Feedback", "Ideas y mejoras", Icons.Rounded.Feedback),
        WiRoute(WiPage.Contacto, "Contacto", "Hablar con el equipo", Icons.Rounded.ContactSupport),
    )
}
