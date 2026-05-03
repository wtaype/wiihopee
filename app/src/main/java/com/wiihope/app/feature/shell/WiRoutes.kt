package com.wiihope.app.feature.shell

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Feedback
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
    val common = listOf(
        WiRoute(WiPage.Descubre, "Descubre", "Explora toda la experiencia WiiHope", Icons.Rounded.Explore),
        WiRoute(WiPage.Blog, "Super Blog", "Reflexiones y testimonios", Icons.Rounded.Article),
        WiRoute(WiPage.OraPorMi, "Ora por mi", "Peticiones de oracion", Icons.Rounded.ChatBubble),
        WiRoute(WiPage.Acerca, "Acerca", "Historia, mision y version", Icons.Rounded.Info),
    )

    fun roleRoutes(role: String): List<WiRoute> = when (role.lowercase()) {
        "admin" -> listOf(
            WiRoute(WiPage.Admin, "Plataforma", "Panel general", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Usuarios, "Usuarios", "Gestion de comunidad", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Mensajes, "Mensajes", "Centro de conversaciones", Icons.Rounded.Mail),
        )
        "gestor", "empresa" -> listOf(
            WiRoute(WiPage.Gestor, "Dashboard", "Resumen de actividad", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Mensajes, "Mensajes", "Centro de conversaciones", Icons.Rounded.Mail),
        )
        else -> listOf(
            WiRoute(WiPage.Descubre, "Dashboard", "Tu espacio WiiHope", Icons.Rounded.Dashboard),
            WiRoute(WiPage.Planificar, "Planificar", "Ideas y devocionales", Icons.Rounded.RocketLaunch),
            WiRoute(WiPage.NuevoPost, "Nuevo Post", "Publicar reflexion", Icons.Rounded.Add),
            WiRoute(WiPage.Notas, "Notas", "Apuntes privados", Icons.Rounded.StickyNote2),
            WiRoute(WiPage.Mensajes, "Mensajes", "Centro de conversaciones", Icons.Rounded.Mail),
        )
    }

    val legal = listOf(
        WiRoute(WiPage.Terminos, "Terminos", "Condiciones de uso", Icons.Rounded.Description),
        WiRoute(WiPage.Privacidad, "Privacidad", "Como cuidamos tus datos", Icons.Rounded.PrivacyTip),
        WiRoute(WiPage.Feedback, "Feedback", "Ideas y mejoras", Icons.Rounded.Feedback),
        WiRoute(WiPage.Contacto, "Contacto", "Hablar con el equipo", Icons.Rounded.ContactSupport),
    )
}
